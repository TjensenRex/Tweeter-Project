package edu.byu.cs.tweeter.server.dao.DynamoDAO;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.util.Pair;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class DynUserDAO extends DynBaseDAO implements UserDAO {
    private DynamoDbTable<UserBean> userTable;
    private DynamoDbTable<UserBean> getUserTable() {
        if (userTable == null) {
            userTable = getClient().table("users", TableSchema.fromBean(UserBean.class));
        }
        return userTable;
    }
    private String hash(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode(password);
        return hash;
    }
    @Override
    public Pair<User, AuthToken> login(LoginRequest request) {
        UserBean user = fetchUser(request.getUsername());
        System.out.println("got user");
        if (user == null) {
            throw new RuntimeException("[Bad Request] username is invalid");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(request.getPassword(), user.getHash())) {
            System.out.println("verified user");
            AuthToken authToken = createAuthToken();
            uploadAuthToken(authToken);
            //System.out.println(authToken.getToken() + " with " + authToken.getTimestamp());
            System.out.println("returning");
            return new Pair<>(user.getUser(), authToken);
        }
        else {
            throw new RuntimeException("[Bad Request] password is invalid");
        }
    }

    private void uploadAuthToken(AuthToken authToken) {
        try {
            //System.out.println("started authtoken upload");
            DynamoDbTable<AuthTokenBean> table = getClient()
                    .table("authToken", TableSchema.fromBean(AuthTokenBean.class));
            AuthTokenBean atBean = new AuthTokenBean(authToken.getToken(), authToken.getTimestamp());
            //System.out.println(atBean.getToken() + " with " + atBean.getLastUsedTimestamp());
            table.putItem(atBean);
        }
        catch (Exception e) {
            throw new RuntimeException("[Server Error] " + e.getMessage());
        }
    }

    private AuthToken createAuthToken() {
        long timestamp = System.currentTimeMillis();
        return new AuthToken(UUID.randomUUID().toString(), timestamp);
    }

    private UserBean fetchUser(String alias) {
        Key key = Key.builder()
                .partitionValue(alias)
                .build();

        // load it if it exists
        UserBean user = getUserTable().getItem(key);
        return user;
    }

    @Override
    public Pair<User, AuthToken> register(RegisterRequest request) {
        String hash = hash(request.getPassword());
        String imageURL = uploadImage(request.getImage(), request.getUsername());
        UserBean user = new UserBean(request.getFirstName(), request.getLastName(), request.getUsername(),
                imageURL, hash);
        AuthToken authToken = createAuthToken();
        // load it if it exists
        UserBean userExists = fetchUser(request.getUsername());
        if(userExists != null) {
            System.out.println(user.getAlias() + "already exists");
            throw new RuntimeException("[Bad Request] User alias already in use.");
        } else {
            getUserTable().putItem(user);
            System.out.println("added user");
        }
        uploadAuthToken(authToken);

        return new Pair<>(user.getUser(), authToken);
    }

    private String uploadImage(String image, String username) {
        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion(Region.US_WEST_1.toString())
                .build();
        System.out.println("opened S3 client");
        byte[] byteArray = Base64.getDecoder().decode(image);

        ObjectMetadata data = new ObjectMetadata();

        data.setContentLength(byteArray.length);

        data.setContentType("image/jpeg");

        PutObjectRequest request = new PutObjectRequest("cs340buckettimrj", username,
                new ByteArrayInputStream(byteArray), data).withCannedAcl(CannedAccessControlList.PublicRead);
        System.out.println("putting image");
        s3.putObject(request);
        System.out.println("image put");
        String link = "URL_HERE" + username;
        return link;
    }

    @Override
    public Pair<Boolean, String> logout(LogoutRequest request) {
        try {
            DynamoDbTable<AuthTokenBean> table = getClient()
                    .table("authToken", TableSchema.fromBean(AuthTokenBean.class));
            Key key = Key.builder()
                    .partitionValue(request.getAuthToken().getToken())
                    .build();
            System.out.println(request.getAuthToken().getToken());
            table.deleteItem(key);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return new Pair<>(true, "Successfully logged out!");
    }

    @Override
    public Pair<Boolean, User> getUser(GetUserRequest request) {
        checkAuthToken(request.getAuthToken());
        UserBean user = fetchUser(request.getAlias());
        if (user == null) {
            return new Pair<>(false, null);
        }
        else {
            return new Pair<>(true, user.getUser());
        }
    }
    @Override
    public void addUserBatch(List<User> users) {
        List<UserBean> batchToWrite = new ArrayList<>();
        String passwordHash = hash("abc123");
        for (User u : users) {
            UserBean dto = new UserBean(u.getFirstName(), u.getLastName(), u.getAlias(), u.getImageUrl(), passwordHash);
            batchToWrite.add(dto);

            if (batchToWrite.size() == 25) {
                // package this batch up and send to DynamoDB.
                writeChunkOfUserDTOs(batchToWrite);
                batchToWrite = new ArrayList<>();
            }
        }

        // write any remaining
        if (!batchToWrite.isEmpty()) {
            // package this batch up and send to DynamoDB.
            writeChunkOfUserDTOs(batchToWrite);
        }
    }
    private void writeChunkOfUserDTOs(List<UserBean> userDTOs) {
        if(userDTOs.size() > 25)
            throw new RuntimeException("Too many users to write");

        WriteBatch.Builder<UserBean> writeBuilder = WriteBatch.builder(UserBean.class).mappedTableResource(getUserTable());
        for (UserBean item : userDTOs) {
            writeBuilder.addPutItem(builder -> builder.item(item));
        }
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            BatchWriteResult result = enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);

            // just hammer dynamodb again with anything that didn't get written this time
            if (result.unprocessedPutItemsForTable(getUserTable()).size() > 0) {
                writeChunkOfUserDTOs(result.unprocessedPutItemsForTable(getUserTable()));
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}

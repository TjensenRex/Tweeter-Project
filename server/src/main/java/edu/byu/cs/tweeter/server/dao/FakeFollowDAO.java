package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * A DAO for accessing 'following' and 'follower' data from the database.
 */
public class FakeFollowDAO implements FollowDAO {

    /**
     * Gets the count of users from the database that the user specified is following. The
     * current implementation uses generated data and doesn't actually access a database.
     *
     * @param follower the User whose count of how many following is desired.
     * @return said count.
     */
    @Override
    public Integer getFolloweeCount(User follower) {
        assert follower != null;
        return getDummyFollowees().size();
    }

    /**
     * Gets the users from the database that the user specified in the request is following. Uses
     * information in the request object to limit the number of followees returned and to return the
     * next set of followees after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param followerAlias the alias of the user whose followees are to be returned
     * @param limit the number of followees to be returned in one page
     * @param lastFolloweeAlias the alias of the last followee in the previously retrieved page or
     *                          null if there was no previous request.
     * @return the followees.
     */
    @Override
    public Pair<List<User>, Boolean> getFollowees(String followerAlias, int limit, String lastFolloweeAlias) {
        assert limit > 0;
        assert followerAlias != null;

        List<User> allFollowees = getDummyFollowees();
        List<User> responseFollowees = new ArrayList<>(limit);

        boolean hasMorePages = false;

        if(limit > 0) {
            if (allFollowees != null) {
                int followeesIndex = getUsersStartingIndex(lastFolloweeAlias, allFollowees);

                for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < limit; followeesIndex++, limitCounter++) {
                    responseFollowees.add(allFollowees.get(followeesIndex));
                }

                hasMorePages = followeesIndex < allFollowees.size();
            }
        }

        return new Pair<>(responseFollowees, hasMorePages);
    }

    /**
     * Determines the index for the first followee in the specified 'allUsers' list that should
     * be returned in the current request. This will be the index of the next followee after the
     * specified 'lastFollowee'.
     *
     * @param lastUserAlias the alias of the last followee that was returned in the previous
     *                          request or null if there was no previous request.
     * @param allUsers the generated list of followees from which we are returning paged results.
     * @return the index of the first followee to be returned.
     */
    @Override
    public int getUsersStartingIndex(String lastUserAlias, List<User> allUsers) {
        int followeesIndex = 0;

        if(lastUserAlias != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allUsers.size(); i++) {
                if(lastUserAlias.equals(allUsers.get(i).getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followeesIndex = i + 1;
                    break;
                }
            }
        }

        return followeesIndex;
    }

    /**
     * Returns the list of dummy followee data. This is written as a separate method to allow
     * mocking of the followees.
     *
     * @return the followees.
     */
    List<User> getDummyFollowees() {
        return getFakeData().getFakeUsers();
    }
    List<User> getDummyFollowers() {
        return getFakeData().getFakeUsers();
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy followees.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return FakeData.getInstance();
    }
    @Override
    public Pair<List<User>, Boolean> getFollowers(String followeeAlias, int limit, String lastFollowerAlias) {
        assert limit > 0;
        assert followeeAlias != null;

        List<User> allFollowers = getDummyFollowers();
        List<User> responseFollowers = new ArrayList<>(limit);

        boolean hasMorePages = false;

        if(limit > 0) {
            if (allFollowers != null) {
                int followersIndex = getUsersStartingIndex(lastFollowerAlias, allFollowers);

                for(int limitCounter = 0; followersIndex < allFollowers.size() && limitCounter < limit; followersIndex++, limitCounter++) {
                    responseFollowers.add(allFollowers.get(followersIndex));
                }

                hasMorePages = followersIndex < allFollowers.size();
            }
        }

        return new Pair<>(responseFollowers, hasMorePages);
    }
    @Override
    public int getFollowerCount(User user) {
        assert user != null;
        return getDummyFollowers().size();
    }
    @Override
    public boolean setFollow(FollowRequest request) {
        assert request.getFollower().getAlias() != null && !Objects.equals(request.getFollower().getAlias(), "");
        assert request.getFollowee() != null;
        if (request.getIsFollowing()) {
            //set follower as a follower of followee
            try{}
            catch(Exception e) {
                return false;
            }
        }
        else {
            //remove followee from follower's following list
            try{}
            catch(Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isFollower(String followerAlias, String followeeAlias) {
        return new Random().nextInt() > 0;
    }

    @Override
    public void addFollowersBatch(List<String> followers, String followTarget) {

    }
}

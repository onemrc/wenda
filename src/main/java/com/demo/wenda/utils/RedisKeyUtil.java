package com.demo.wenda.utils;

public class RedisKeyUtil {

    private static final  String SPILT = ":";

    // 实体下的粉丝(关注者)
    private static final String FOLLOWER = "FOLLOWER";

    // 用户所关注的实体(关注对象)
    private static final String FOLLOWEE = "FOLLOWEE";



    /**
     * 每个实体(作为key)，粉丝存入该key对应的值中
     *
     * example:"FOLLOWER:1:7" 这个key
     * (1：User)
     * (7:userId)
     *
     * 这个有序集合中包含了所有关注了 userId=7 的用户Id
     *
     * @param entityType
     * @param entityId
     * @return
     */
    public static String getFollowerKey(int entityType,int entityId){
        return FOLLOWER+SPILT+entityType+SPILT+entityId;
    }


    /**
     * 一个用户，关注了某一类实体,如用户、问题等(由userId与entityType共同组成key)
     *
     * example:"FOLLOWER:6:1" 这个key
     * (6：userId)
     * (1:User)
     *
     * 这个有序集合中包含了userId=6 的用户 关注的其他用户的userId
     *
     * @param userId
     * @param entityType
     * @return
     */
    public static String getFolloweeKey(int userId,int entityType){
        return FOLLOWEE+SPILT+userId+SPILT+entityType;
    }
}

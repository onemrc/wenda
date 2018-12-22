package com.demo.wenda.utils;

public class RedisKeyUtil {

    private static final  String SPILT = ":";

    // 实体下的粉丝(关注者)
    private static final String FOLLOWER = "FOLLOWER";

    // 用户所关注的实体(关注对象)
    private static final String FOLLOWEE = "FOLLOWEE";

    //实体下的赞
    private static final String LIKE_COMMENT = "LIKE_COMMENT";

    //用户点赞的实体
    private static final String USER_LIKE="USER_LIKE";

    //异步事件队列
    private static final String EVENT_QUEUE = "EVENT_QUEUE";

    //问题标签
    private static final String QUESTION_TAG = "QUESTION_TAG";

    //标签下的问题
    private static final String TAG_QUESTION = "TAG_QUESTION";

    //某问题的浏览数
    private static final String QUESTION_LOOK="QUESTION_LOOK";

    private static final String TIMELINE = "TIMELINE";



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
     * example:"FOLLOWEE:6:1" 这个key
     * (6：userId)
     * (1:User)
     *
     * 这个有序集合中包含了userId=6 的用户 被关注的其他用户的userId
     *
     * @param userId
     * @param entityType
     * @return
     */
    public static String getFolloweeKey(int userId,int entityType){
        return FOLLOWEE+SPILT+userId+SPILT+entityType;
    }


    /**
     * 某个评论下，点赞的人
     *
     * entityType: 2-COMMENT,3-ANSWER
     *
     *  example:"LIKE_COMMENT:3:1" 这个key
     *  (3:ANSWER)
     *  (1:commentId)
     *  commentId = 1 的回答下，点赞的userId
     *
     *  example:"LIKE_COMMENT:2:2" 这个key
     *  (2:COMMENT)
     *  (2:commentId)
     *  包含commentId = 2 的评论下，点赞的userId
     *
     * @param commentId 评论id
     * @param entityType 评论实体类型
     * @return
     */
    public static String getCommentLikeKey(int entityType,int commentId){return LIKE_COMMENT +SPILT+entityType+SPILT+commentId;}


    /**
     * 一个用户，给哪个评论或回答点赞
     *
     * entityType: 2-COMMENT,3-ANSWER
     *
     * example:"USER_LIKE:1:2" 这个key
     * （1:userId）
     *（2:COMMENT）
     * 包含userId=1 的用户下，点赞的评论id
     *
     * @param userId
     * @param entityType
     * @return
     */
    public static String getUserLikeKey(int userId,int entityType){return USER_LIKE+SPILT+userId+SPILT+entityType;}


    /**
     * 异步事件队列
     * 表示有多少异步消息队列待处理
     * @return
     */
    public static String getEventQueueKey(){
        return EVENT_QUEUE;
    }

    /**
     * 某问题所包含的标签id
     *
     * example:"QUESTION_TAG:1" 这个key
     * 包含questionId=1的问题下有多少个tagId
     *
     * @param questionId 问题id
     * @return
     */
    public static String getQuestionTagKey(int questionId){
        return QUESTION_TAG+SPILT+questionId;
    }

    /**
     * 某标签下的问题
     *
     * example:"TAG_QUESTION:1" 这个key
     * 包含tagId=1的问题下的questionId
     *
     * @param tagId 标签id
     * @return
     */
    public static String getTagQuestionKey(int tagId){
        return TAG_QUESTION+SPILT+tagId;
    }

    /**
     * 某问题的浏览数
     *
     * example:"QUESTION_LOOK:1" 这个key
     * 包含 questionId=1 的问题浏览数
     *
     * @param questionId
     * @return
     */
    public static String getQuestionLook(int questionId){
        return QUESTION_LOOK+SPILT+questionId;
    }


    public static String getTimelineKey(int userId) {
        return TIMELINE + SPILT + String.valueOf(userId);
    }
}

# API


### 热门问答列表

```
GET /hot
```

参数

```
无
```

返回

```
{
    "code": 200,
    "msg": "成功",
    "data": [
        {
            "question_tiele": "让你用得最爽的 Windows 软件是什么？",
            "question_content": "求解balalaaasdasds",
            "question_id": 1,
            "comment_count":1000,
            "user_name":"abc",
            "user_header":"",
            "look_count":100000,
        },
        {
             "question_tiele": "让你用得最爽的 Windows 软件是什么？",
             "question_content": "求解balalaaasdasds",
             "question_id": 1,
             "comment_count":1000,
             "user_name":"abc",
             "user_header":"",
             "look_count":100000,
         },
    ]
}
```


### 最新消息列表

```
GET /new
```

参数

```
无
```

返回

```
{
    "code": 200,
    "msg": "成功",
    "data": [
        {
            "question_tiele": "让你用得最爽的 Windows 软件是什么？",
            "create_date":2018-11-10 14:20,
            "tag":[
            {
                  "tag_name":"编程",
                  "tag_id":1
            },
             {
                  "tag_name":"编程",
                  "tag_id":1
              },
              ]
            "comment_count":2,
            "follow_count":200,
            
            "user_name":"sdad",
            "user_id":1,
            "auth_name":"在校学生"
        },
        {
           "question_tiele": "让你用得最爽的 Windows 软件是什么？",
           "create_date":2018-11-10 14:20,
           "tag":[
              {
                 "tag_name":"编程",
                 "tag_id":1
              },
              {
                 "tag_name":"软件",
                 "tag_id":2
               },
                  ]
            "comment_count":2,
            "user_name":"abc",
            "user_id":2,
            "auth_name":"在校学生"
       },
      
    ]
}
```


### 添加问题

```
POST /question/addQuestion
```

参数

```
"question_title":"让你用得最爽的 Windows 软件是什么？",
"question_content":"求解balalaaasdasds",
"tag_name":"软件",
"anonymous":0
```

返回

```
 "code": 200,
 "msg": "成功",
```


### 问题详情页

```
GET /{question_id}
```

参数

```

```

返回

```
 "code": 200,
 "msg": "成功",
 "data": [
     "question_title":"让你用得最爽的 Windows 软件是什么？",
     "question_content":"求解balalaaasdasds",
     "tag_name":"软件",
     "anonymous":0
     
     comment:[
      {
            "comment_content": "我觉得baabaa",
            "create_date":2018-11-10 14:20,
            "comment_count":2,     //评论数
            "comment_id":30,         //评论id
            "like_count":20000,
                     
            "user_name":"sdad",
            "user_id":1,
            "auth_name":"在校学生"
           },
     ]
    
 ]
```

### 给回答点赞

```
GET /addLikeToAnswer
```

参数

```
commentId:2
```

返回

```
 "code": 200,
 "likeCount": 10,
```

### 给回答取消点赞

```
GET /cancelLikeToAnswer
```

参数

```
commentId:2
```

返回

```
 "code": 200,
 "msg": "成功",
```

### 添加回答

```
GET /addAnswer
```

参数

```
commentId:2
```

返回

```
 "code": 200,
 "msg": "成功",
```


### 发私信

```
POST /msg/add
```

参数

```
toName:"陈一"
conten:"你好啊！balabalaa"
```

返回

```
 "code": 200,
 "msg": "成功",
```

### 收藏问题

```
POST /addQuestionCollection
```

参数

```
questionId:2
```

返回

```
 "code": 200,
 "msg": "成功",
```
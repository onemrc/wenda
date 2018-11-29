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
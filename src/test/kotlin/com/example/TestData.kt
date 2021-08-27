package com.example

const val postInvalidSubscriptionRequest = """{
     "userID": "ac6997eb-c251-4770-9eb7-b6a78c031061",
     "topics": []
  }
"""

const val postValidSubscriptionRequest = """{
     "userID": "$",
     "topics": ["e-commerce"],
     "platform": "android-plus10",
     "token": "test"
  }
"""

const val invalidPushUserRequest = """{
    "message": {
        "title": "test",
        "body": "new notification",
        "image": "https://www.strathmore.edu/wp-content/uploads/2021/01/Jabez-Magomere-1.jpg",
        "isHighPriority": true
    }
}"""

const val validPushUserRequest = """{
    "userID": "$",
    "message": {
        "notification" : {
            "body": "new kotlin language is out",
            "title":  "test",
            "image": "https://www.strathmore.edu/wp-content/uploads/2021/01/Jabez-Magomere-1.jpg"
        },
        "isHighPriority": true
    }
}"""
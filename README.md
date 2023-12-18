# messenger-system
This is a peer to peer messenger system where a user can either send or receive messages at a time once he/she login to the system.
Another title will be Features provided through APIs
### Create a new user
```
curl --location 'http://localhost:8080/user' \
--header 'Content-Type: application/json' \
--header 'Cookie: CTK=ff9c1d35-c61c-4e10-94f6-15ce156cb652' \
--data-raw '{
    "username":"mohitsmr",
    "password":"Mohit@1234"
}'
```
### login user
```
curl --location 'http://localhost:8080/login' \
--header 'Content-Type: application/json' \
--header 'Cookie: CTK=ff9c1d35-c61c-4e10-94f6-15ce156cb652' \
--data-raw '{
    "username":"mohitsmr",
    "password":"Mohit@1234"
}'
```
### Get All users created 
```
curl --location 'http://localhost:8080/user' \
--header 'Cookie: CTK=ff9c1d35-c61c-4e10-94f6-15ce156cb652'
```
### A user send message to another user
```
curl --location 'http://localhost:8080/user/mohitsmr/message' \
--header 'Content-Type: application/json' \
--header 'Cookie: CTK=ff9c1d35-c61c-4e10-94f6-15ce156cb652' \
--data '{
    "to":"mohitsmr23",
    "text":"Hiiii there2323!!!"
}'
```
### Get unread messages for a given user
```
curl --location 'http://localhost:8080/user/mohitsmr/unread/messages' \
--header 'Cookie: CTK=ff9c1d35-c61c-4e10-94f6-15ce156cb652'
```
### Get chat history between two users
```
curl --location 'http://localhost:8080/user/mohitsmr/chat/history?friend=mohitsmr23' \
--header 'Cookie: CTK=ff9c1d35-c61c-4e10-94f6-15ce156cb652'
```
### Logout a user
```
curl --location 'http://localhost:8080/logout' \
--header 'Content-Type: application/json' \
--header 'Cookie: CTK=ff9c1d35-c61c-4e10-94f6-15ce156cb652' \
--data '{
    "username":"mohitsmr"
}'
```
# Assumptions
1. I have used in memory to store data for messenger-system instead of database for keeping it simple from dependency point of view and running service easily by different users
2. I have changed the api path for get unread messages and chat history as the paths that was provided in assignment were same and conflicting while running spring boot application
3. If you want to use api for send message , get unread messages and get chat history the two users must be be login using login api if both of them has already been created.These cases have been taken care in code you will get message like "failure , please login " 
4. Once you hit the get unread messages of a user api then again if you hit the api without logout that user in the existing session then it will give "No new messages" as i am assuming after hitting get unread messages for the first time it means the user has already read all those messages.
5. For get chat history if a user1 has send message to user2 and you fetch chat history of chat between user1 and ? friend=user2 then in response it will show "user1: "message sent by user1 to user2", "user2":" the text for user2 will be empty since user2 has not send any message to user1 .Once user2 sends message to user1 then the message will reflect like "user1: "message sent by user1 to user2", "user2":"Message sent by user2 to user1""

# Requirements and Steps to Run the Application
1. Clone the repository from below github url provided below
 ```https://github.com/Mohitsmr23/messenger-system```
2. You should have docker desktop installed on your local.You can use below link to download the docker desktop suitable for your local
```https://www.docker.com/products/docker-desktop/```
3. You should have api platform like Postman to test and run APIs.I will recommend Postman to download as it is user friendly and easy to use.You can use below link to download the Postman which suits your local 
```https://www.postman.com/downloads/```
4. Once you download the Postman you can import all the curls shared above
5. Now since all the step ups are there go to your location where you have cloned the github repository mentioned in step1 using your terminal(Mac) command prompt(Windows) using ```cd``` command like below:
```cd messenger-system```
6. Again do ```cd messenger-system``` this will make sure you have reached to the root folder
7. To make sure you have reached to the root folder type ```ls``` which will list all the files and directories exists and see whether Dockerfile is present in it or not if it is then you are at root folder.
Note ```ls``` is used for Mac and for windows you can use ```dir``` 
8. Now run you docker desktop
9. Run command  ```docker build -t messenger-system .```
10. Run command ```docker images``` 
11. Copy the image name or repository name messenger-system 
12. Once image is built successfully you can run command ```docker run -p 8080:8080 messenger-system```
13. After successfully doing all the step you can see your spring boot application running 
14. Start hitting APIs from postman to test it 
## Note: 
you can give any image name of your choice in place of messenger-system used in command ```docker build -t messenger-system .``` (Note after messenger-system . is a part of command that you need to run in terminal).Similarly in command ```docker run -p 8080:8080 messenger-system``` you can replace messenger-system with name of image that you have used while building your docker image using ```docker build -t <name-of-image> .``` Replace whole <name-of-image> with name of image you want to give

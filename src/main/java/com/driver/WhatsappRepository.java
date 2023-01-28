package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public String createUser(String name, String mobileNO) throws Exception{
        if(userMobile.contains(mobileNO)){
            throw new Exception("Already Exists");
        }else {
            userMobile.add(mobileNO);
        }

        User user = new User(name,mobileNO);
        user.setName(name);
        user.setMobile(mobileNO);

        return "SUCCESS";
    }

    public Group createGroup(List<User> users) throws Exception{
        int userCount = 0;
        User adminName = users.get(0);
        if(users.size() == 2){
            User user = users.get(users.size()-1);
            String groupName = user.getName();
            Group group = new Group(groupName,users.size());
            groupUserMap.put(group,users);
            adminMap.put(group,adminName);
        }
        userCount++;
        Group group = new Group("Group" + userCount,users.size());
        groupUserMap.put(group,users);
        adminMap.put(group,adminName);
        return group;
    }


    public int createMessage(String content){
        messageId++;
        Message message = new Message(messageId,content,new Date());
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception{
        if(!groupUserMap.containsKey(group.getName())){
            throw new Exception("Group Not Available");
        }
        List groupName = groupUserMap.get(group.getName());
        if(!groupName.contains((sender.getName()))){
            throw new Exception("Not able to send the message");
        }
        return messageId;
    }
    public String changeAdmin(User approver, User user, Group group) throws Exception{
        if(!groupUserMap.containsKey(group.getName())){
            throw new Exception("Not Exists");
        }
        if(adminMap.get(group).getName() != approver.getName()){
            throw new Exception("User not part of group");
        }
        adminMap.put(group,user);
        return "SUCCESS";
    }
    public int removeUser(User user) throws Exception{
        if(!groupUserMap.containsKey(user)){
            throw new Exception("user Not available");
        }

        if(adminMap.containsKey(user)){
            throw new Exception("User is admin not removed from group");
        }
        groupUserMap.remove(user);
        groupMessageMap.remove(user);
        return (groupUserMap.size() + groupMessageMap.size() + senderMap.size());
    }
//    public String findMessage(Date start, Date end, int K) throws Exception{
//
//    }
}

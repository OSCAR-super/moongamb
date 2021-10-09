package com.lyt.moongamb.webSocket;

import com.lyt.moongamb.util.security.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;


@ServerEndpoint(value = "/socket/{platformType}/{username}/{token}")
@Component
@Slf4j
public class WebsocketRoom {

    private static JwtTokenUtils jwtTokenUtils;
    @Autowired
    public void setChatService(JwtTokenUtils jwtTokenUtils) {
        WebsocketRoom.jwtTokenUtils = jwtTokenUtils;
    }

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    public static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    public static CopyOnWriteArraySet<WebsocketRoom> webSocketSet = new CopyOnWriteArraySet<WebsocketRoom>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    public Session session;

    //接收参数中的用户ID
    public String username;

    //接收用户中的平台类型
    public String platformType;


    /**
     * 连接建立成功调用的方法
     * 接收url中的参数
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("platformType") String platformType, @PathParam("username") String username, @PathParam("token") String token) {
        try {
            if (!jwtTokenUtils.checkRedisBlack(token)){
                log.warn(token + "  无效token....");
                throw new RuntimeException("无效token");
            }
        }catch (Exception e){
            e.printStackTrace();
            log.warn(token + "  无效token....");
            throw new RuntimeException("无效token");
        }
        String userName = jwtTokenUtils.getAuthAccountFromToken(token);

        log.info(userName + "准备进入连接.....");
        this.session = session;
        this.username = username;
        this.platformType = platformType;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        log.info("有新连接加入！当前在线人数为" + getOnlineCount() + "  username==== " + username + "  platformType==== " + platformType);
//        try {
//            //sendMessage("连接成功");
//        } catch (IOException e) {
//            log.error("websocket IO异常");
//        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session,@PathParam("username") String username, @PathParam("platformType") String platformType) throws IOException {
        log.info("来自客户端的消息:" + message);
        Random rand = new Random();
        List<Integer>integers=new ArrayList<>();
        StringBuilder messageBuilder = new StringBuilder(username+" ");
        messageBuilder.append("摇到了:");
        for (int i = 0; i<6; i++){
            int a=rand.nextInt(6)+1;
            integers.add(a);
            messageBuilder.append(" ").append("<img src=\"../../resource/s").append(a).append(".png\">");
        }
        //message="摇到了："+(rand.nextInt(6)+1)+" "+(rand.nextInt(6)+1)+" "+(rand.nextInt(6)+1)+" "+(rand.nextInt(6)+1)+" "+(rand.nextInt(6)+1)+" "+(rand.nextInt(6)+1)+"<br/>";
        if (integers.contains(4)&&!integers.contains(1)&&!integers.contains(2)&&!integers.contains(3)&&!integers.contains(4)&&!integers.contains(5)){
            messageBuilder.append(" ").append("六杯红！");
        }else if (integers.contains(1)&&!integers.contains(1)&&!integers.contains(2)&&!integers.contains(3)&&!integers.contains(4)&&!integers.contains(5)){
            messageBuilder.append(" ").append("遍地锦！");
        }else if (integers.contains(6)&&!integers.contains(1)&&!integers.contains(2)&&!integers.contains(3)&&!integers.contains(4)&&!integers.contains(5)){
            messageBuilder.append(" ").append("六杯黑！");
        }else if (integers.contains(6)&&integers.contains(1)&&integers.contains(2)&&integers.contains(3)&&integers.contains(4)&&integers.contains(5)){
            messageBuilder.append(" ").append("对堂！");
        }else{
            int four=0;
            int one =0;
            int two =0;
            int five=0;
            for (Integer integer:integers){
                if (integer.equals(4)){
                    four++;
                }else if (integer.equals(1)){
                    one++;
                }else if (integer.equals(2)){
                    two++;
                }else if (integer.equals(5)){
                    five++;
                }
            }
            if (five==5){
                messageBuilder.append(" ").append("五子登科！");
            }else if (four==1){
                messageBuilder.append(" ").append("一秀！");
            }else if (two==4){
                messageBuilder.append(" ").append("四进！");
            }else if (four==3){
                messageBuilder.append(" ").append("三红！");
            }else if (four==2){
                messageBuilder.append(" ").append("二举！");
            }else if (four==4&&one==2){
                messageBuilder.append(" ").append("金花！");
            }else if (four==4){
                messageBuilder.append(" ").append("四点红！");
            }else if (four==5){
                messageBuilder.append(" ").append("五红！");
            }
        }
        //this.session.getBasicRemote().sendText(messageBuilder.toString());
        sendInfos(messageBuilder.toString(),platformType);
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误" + error);
        error.printStackTrace();
    }


    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 群发自定义消息
     */
    public static void sendInfos(String message,String platformType) throws IOException {
        log.info(message);
        for (WebsocketRoom item : webSocketSet) {
            try {
                if (item.platformType.equals(platformType)){
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebsocketRoom.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebsocketRoom.onlineCount--;
    }


}

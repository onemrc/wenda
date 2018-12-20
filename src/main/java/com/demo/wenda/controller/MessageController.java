package com.demo.wenda.controller;

import com.demo.wenda.domain.HostHolder;
import com.demo.wenda.domain.Message;
import com.demo.wenda.domain.User;
import com.demo.wenda.enums.ReadStatus;
import com.demo.wenda.service.MessageService;
import com.demo.wenda.service.UserService;
import com.demo.wenda.utils.ConverterUtil;
import com.demo.wenda.vo.ViewObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 站内信
 */
@Controller
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    private MessageService messageService;

    private HostHolder hostHolder;

    private UserService userService;


    @Autowired
    public MessageController(MessageService messageService, HostHolder hostHolder, UserService userService) {
        this.messageService = messageService;
        this.hostHolder = hostHolder;
        this.userService = userService;
    }


    /**
     * 发私信
     * @param toName 接收者昵称
     * @param content 内容
     * @return
     */
    @PostMapping(value = {"/add"})
    @ResponseBody
    public String add(@RequestParam("toName") String toName,
                      @RequestParam("content") String content){

        try{
            //私信接受方，信息
            User user = userService.getUserByName(toName);

            //本地用户
            User localUser = hostHolder.getUsers();

            if (user == null){
                return ConverterUtil.getJSONString(1,"没有这个用户");
            }

            Message message = new Message();


            message.setContent(content);

            // 私信的fromId为当前登录用户的Id
            message.setFromId(localUser.getUserId());

            message.setToId(user.getUserId());

            message.setCreateDate(new Date());

            message.setConversationId();

            message.setHasRead(ReadStatus.NOT_READ.getCode());

            messageService.addMessage(message);

            return ConverterUtil.getJSONString(200,"发送成功");
        }catch (Exception e){
            logger.error("私信发送异常：{}",e.getMessage());
            return ConverterUtil.getJSONString(201,"私信发送失败");
        }
    }

    @GetMapping(value = {"/msg/list"})
    public String list(Model model){
        if (hostHolder.getUsers() == null){
            ConverterUtil.getJSONString(999);
        }else {
            model.addAttribute("localUser",hostHolder.getUsers());
        }

        List<Message> conversationList = messageService.getConversationList(hostHolder.getUsers().getUserId(),0,10);

        List<ViewObject> conversations = new ArrayList<ViewObject>();
        for (Message message:conversationList){
            ViewObject vo = new ViewObject();
            vo.set("message",message);

            //和当前用户联系起来的另外一个用户的id
            int targetId = message.getFromId() == hostHolder.getUsers().getUserId()?message.getToId():message.getFromId();
            vo.set("user",userService.getById(targetId));
            //还有多少条未读信息
            vo.set("unReadCount",messageService.getConversationUnReadCount(hostHolder.getUsers().getUserId(),message.getConversationId()));

            //这两个人有多少条会话
            vo.set("conversationCount",messageService.getConversationCount(message.getConversationId()));
            conversations.add(vo);
        }
        model.addAttribute("conversations",conversations);
        return "letter";
    }
}

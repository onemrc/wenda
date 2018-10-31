package com.demo.wenda.service;

import com.alibaba.druid.util.StringUtils;
import org.apache.commons.lang3.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class SensitiveService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);

    //敏感词替换字符
    private static final String DEFAULT_REPLACEMENT = "***";

    /**
     * 节点数据结构
     */
    private class TrieNode{

        //true == 关键词的终结
        private boolean end = false;

        //key == 下一个字符，value == 对应的节点
        private Map<Character,TrieNode> subNodes = new HashMap<>();

        /*
        向指定位置添加节点树
         */
        void  addSubNode(Character key, TrieNode value){
            subNodes.put(key,value);
        }

        /*
        获取下一个节点
         */
        TrieNode getSubNode(Character key){
            return subNodes.get(key);
        }

        boolean isKeyWordEmd(){
            return end;
        }

        void setKeywordEnd(boolean end) {
            this.end = end;
        }

        public int getSubNodeCount() {
            return subNodes.size();
        }
    }

    /*
    判断是否为一个符号
     */
    public boolean isSymbol(char c){
        int ic = (int) c;

        // 0x2E80-0x9FFF 东亚文字范围 (字符不在这个范围内，就认为它是一个符号，不属于文字)
        return CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    //根节点
    private TrieNode rootNode = new TrieNode();

    /*
    敏感词过滤
     */
    public String filter(String text){
        if (StringUtils.isEmpty(text)){
            return text;
        }

        String replacement = DEFAULT_REPLACEMENT;
        StringBuffer result = new StringBuffer();

        TrieNode tempNode = rootNode;
        int begin = 0; //回滚数
        int position = 0;// 当前位置

        while (position < text.length()){
            char c = text.charAt(position);

            //空格、符号直接跳过
            if (isSymbol(c)){
                if (tempNode == rootNode){
                    result.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }


            tempNode = tempNode.getSubNode(c);

            //当前位置的匹配结束
            if (tempNode == null){
                //以begin开始的字符串不存在敏感词
                result.append(text.charAt(begin));

                //调到下一个字符开始测试
                position = begin+1;
                begin = position;

                //回到树初始节点
                tempNode = rootNode;
            }else if (tempNode.isKeyWordEmd()){//发现敏感词
                //用replacement 替换掉 从 begin-position 的字符
                result.append(replacement);
                position+= 1;
                begin = position;
                tempNode = rootNode;
            }else {
                ++position;
            }
        }

        //加上最后的字符
        result.append(text.substring(begin));

        return result.toString();

    }

    private void addWord(String lineText){
        TrieNode tempNode = rootNode;
        //循环每一个字节
        for (int i=0;i<lineText.length();i++){
            Character c = lineText.charAt(i);

            //过滤空格
            if (isSymbol(c)){
                continue;
            }
            TrieNode node = tempNode.getSubNode(c);

            if (node == null){//没初始化
                node = new TrieNode();
                tempNode.addSubNode(c,node);
            }

            tempNode = node;

            if (i == lineText.length()-1){
                //关键词结束，设置结束标志
                tempNode.setKeywordEnd(true);
            }
        }
    }


    /*
    读取敏感词文件
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        rootNode = new TrieNode();

        try{
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");

            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String lineText;
            while ((lineText = bufferedReader.readLine()) != null){
                //移走前后空格
                lineText = lineText.trim();

                addWord(lineText);
            }
            reader.close();
        }catch (Exception e){
            logger.error("读取敏感词文件异常：{}",e.getMessage());
        }
    }


//    public static void main(String[] args) {
//        SensitiveService s = new SensitiveService();
//        s.addWord("色情");
//        s.addWord("好色");
//        System.out.print(s.filter("你好X色情XX"));
//    }
}

package com.emily.connect.sample.client;

import com.emily.connect.client.ClientConnection;
import com.emily.connect.client.ClientProperties;
import com.emily.connect.core.db.DbModelItem;
import com.emily.connect.core.protocol.TransContent;
import com.emily.connect.core.protocol.TransHeader;
import com.emily.connect.core.utils.UUIDUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;

/**
 * @program: SkyDb
 * @description: RPC服务调用
 * @author: Emily
 * @create: 2021/09/18
 */

public class ConnectClientBootStrap {
    private static final ClientConnection connection = new ClientConnection(new ClientProperties());

    public static void main(String[] args) {

        for (int i = 0; i < 5000; i++) {
            try {
                Object list = selectBody(String.valueOf(i));

                System.out.println(i + "---------false");
                Thread.sleep(1000 * 60 * 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static Object selectBody(String i) {
        try {
            TransContent transContent = new TransContent();
            transContent.dbName = "account";
            transContent.dbTag = "select_test_dual";
            transContent.params.add(new DbModelItem("testText", i));
            Object list = executeQuery(transContent);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 查询操作
     *
     * @param transContent 请求参数
     * @return
     * @throws Exception
     */
    public static Object executeQuery(TransContent transContent) throws Exception {
        TransHeader transHeader = new TransHeader(UUIDUtils.randomSimpleUUID());
        List<Map<String, DbModelItem>> list = connection.sendRequest(transHeader, transContent, new TypeReference<>() {
        });
        return list;
    }
}

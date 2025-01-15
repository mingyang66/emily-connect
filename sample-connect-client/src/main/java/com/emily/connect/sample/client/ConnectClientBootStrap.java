package com.emily.connect.sample.client;

import com.emily.connect.client.ClientConnection;
import com.emily.connect.client.ClientProperties;
import com.emily.connect.core.protocol.RequestBody;
import com.emily.connect.core.protocol.RequestHeader;
import com.emily.connect.core.utils.UUIDUtils;
import com.fasterxml.jackson.core.type.TypeReference;

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
                Thread.sleep(1000 * 6 * 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static Object selectBody(String i) {
        try {
            RequestBody requestBody = new RequestBody();
            requestBody.setUsername("account");
            requestBody.setPassword("select_test_dual");
            requestBody.setAge(10);
            Object list = executeQuery(requestBody);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询操作
     *
     * @param requestBody 请求参数
     * @return
     * @throws Exception
     */
    public static Object executeQuery(RequestBody requestBody) throws Exception {
        RequestHeader requestHeader = new RequestHeader()
                .traceId(UUIDUtils.randomSimpleUUID())
                .appType("com.android")
                .appVersion("6.8")
                .systemNumber("Emily-Sdk");
        return connection.getForEntity(requestHeader, requestBody, new TypeReference<>() {
        });
    }
}

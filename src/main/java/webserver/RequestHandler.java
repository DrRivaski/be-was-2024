package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        Response response = new Response();
        LogicProcessor logicProcessor = new LogicProcessor();

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            while (!Thread.currentThread().isInterrupted()) {
                // InputStream을 BufferedReader로 변환
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                DataOutputStream dos = new DataOutputStream(out);

                // 읽어들인 InputStream 모두 출력
                String requestLine = br.readLine();
                Request request = new Request(requestLine);
                String method = request.getHttpMethod();

                if (method.equals("GET")) {
                    if (request.isQueryString()) {
                        logicProcessor.createUser(request);
                        response.redirect("/index.html", dos);
                    } else {
                        response.response(request.getStaticPath(), dos);
                    }
                }

                String line;
                while (!(line = br.readLine()).isEmpty()) {
                    logger.debug(line); // 읽어들인 라인 출력
                }
//                String line; // 읽어들일 라인
//                String url = ""; // 요청 파일
//                while (!(line = br.readLine()).isEmpty()) {
//                    logger.debug(line); // 읽어들인 라인 출력
//                    // 요청이 GET일 경우
//                    if (urlParser.getHttpMethod(line).equals("GET")) {
//                        // 요청 파일 받아오기
//                        url = urlParser.getURL(line);
//                        responseHandler.getResponse(url, dos);
//                    }
//                }

                // url로부터 html파일을 byte array로 읽어오기
//                byte[] body = resourceHandler.getByteArray(url);
//
//                response200Header(dos, body.length, resourceHandler.getContentType(url));
//                responseBody(dos, body);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

//    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
//        try {
//            dos.writeBytes("HTTP/1.1 200 OK \r\n");
//            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
//            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
//            dos.writeBytes("\r\n");
//        } catch (IOException e) {
//            logger.error(e.getMessage());
//        }
//    }
//
//    private void responseBody(DataOutputStream dos, byte[] body) {
//        try {
//            dos.write(body, 0, body.length);
//            dos.flush();
//        } catch (IOException e) {
//            logger.error(e.getMessage());
//        }
//    }
}

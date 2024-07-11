package distributor;

import handler.SessionHandler;
import processor.ResponseProcessor;
import processor.UserProcessor;
import webserver.Request;

import java.io.DataOutputStream;
import java.io.IOException;

public class PostDistributor extends Distributor {
    Request request;
    DataOutputStream dos;
    UserProcessor userProcessor = new UserProcessor();

    protected PostDistributor(Request request, DataOutputStream dos) {
        this.request = request;
        this.dos = dos;
    }

    @Override
    public void process() throws IOException {
        processQuery(this.dos);
    }

    private void processQuery(DataOutputStream dos) throws IOException {
        String path = request.getPath();
        if (path.equals("/user/create")) {
            processUserCreate(dos);
        } else if (path.equals("/user/login")) {
            processUserLogin(dos);
        }
    }

    private void processUserCreate(DataOutputStream dos) throws IOException {
        userProcessor.createUser(request);

        ResponseProcessor responseProcessor = ResponseProcessor.from(dos);
        responseProcessor.createUserResponse();
    }

    private void processUserLogin(DataOutputStream dos) throws IOException {
        if (userProcessor.login(request)) {
            String sessionId = SessionHandler.getSessionId(request.parseBody().get("userId"));

            ResponseProcessor responseProcessor = ResponseProcessor.from(dos);
            responseProcessor.loginSuccessResponse(sessionId);
        } else {
            ResponseProcessor responseProcessor = ResponseProcessor.from(dos);
            responseProcessor.loginFailedResponse();
        }
    }
}

package uk.flypi.drone.ctrl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CmdBroker {
    

    public CompletableFuture<CmdResult> processCmd(final List<CmdRequest> cmdRequests) {

        return CompletableFuture.completedFuture(CmdResult.SUCESS);
    }

    enum CmdResult {
        SUCESS,
        FAILURE
    }
}

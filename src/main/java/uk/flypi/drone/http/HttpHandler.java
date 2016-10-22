package uk.flypi.drone.http;

import com.google.gson.Gson;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import uk.flypi.drone.ctrl.CmdRequest;

import static io.netty.buffer.Unpooled.copiedBuffer;

public class HttpHandler extends ChannelInboundHandlerAdapter {
    private final Gson gson;

    public HttpHandler() {
        this.gson = new Gson();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            final FullHttpRequest request = (FullHttpRequest) msg;
            final FullHttpResponse response;

            if (!request.getMethod().equals(HttpMethod.POST)) {
                response = buildDefaultResponse(HttpResponseStatus.METHOD_NOT_ALLOWED, null);
                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, 0);
            } else {
                final String requestMsg = request.content().toString(CharsetUtil.UTF_8);
                final CmdRequest cmdRequest = gson.fromJson(requestMsg, CmdRequest.class);
                final String responseMessage = "Hello from FlyPi!";
                response = buildDefaultResponse(HttpResponseStatus.OK, responseMessage);

//            if (HttpHeaders.isKeepAlive(request)) {
//                response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
//            }
                response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain");
                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, responseMessage.length());

            }


            ctx.writeAndFlush(response);
        } else {
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.writeAndFlush(new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.INTERNAL_SERVER_ERROR,
                copiedBuffer(cause.getMessage().getBytes())
        ));
    }

    private FullHttpResponse buildDefaultResponse(final HttpResponseStatus status, final String message) {
        if (message != null) {
            return new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    status,
                    copiedBuffer(message.getBytes())
            );
        } else {
            return new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    status
            );
        }
    }

}

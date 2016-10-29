package uk.flypi.drone.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpHandlerTest {
    private static final CharSequence CONNECTION_ENTITY = HttpHeaders.newEntity("Connection");
    private static final CharSequence CLOSE_ENTITY = HttpHeaders.newEntity("close");

    private HttpHandler underTest;
    @Mock private ChannelHandlerContext ctx;
    @Mock private FullHttpRequest request;
    @Mock private ByteBuf requestContent;
    @Mock private HttpHeaders requestHeaders;

    @Before
    public void setUp() throws Exception {
        this.underTest = new HttpHandler();
        when(request.content()).thenReturn(requestContent);
        when(request.headers()).thenReturn(requestHeaders);

        when(request.getMethod()).thenReturn(HttpMethod.POST);
        when(requestHeaders.get("Connection")).thenReturn(null);
    }

    @Test
    public void itIsHappy() throws Exception {
        when(requestContent.toString()).thenReturn("{\"msg\": \"this is a test\"}");
        ArgumentCaptor<FullHttpResponse> responseCaptor = ArgumentCaptor.forClass(FullHttpResponse.class);

        this.underTest.channelRead(ctx, request);

        verify(request).getMethod();
        verify(request).content();
        verify(requestContent).toString(StandardCharsets.UTF_8);
        verify(ctx).writeAndFlush(responseCaptor.capture());
        verifyNoMoreInteractions(ctx, request, requestContent, requestHeaders);

        final FullHttpResponse actual = responseCaptor.getValue();
        assertEquals(200, actual.getStatus().code());
        assertEquals("Hello from FlyPi!", actual.content().toString(StandardCharsets.UTF_8));
    }

    @Test
    public void itRejectsARequestCONNECT() throws Exception {
        itRejectsARequest(HttpMethod.CONNECT);
    }

    @Test
    public void itRejectsARequestDELETE() throws Exception {
        itRejectsARequest(HttpMethod.DELETE);
    }

    @Test
    public void itRejectsARequestHEAD() throws Exception {
        itRejectsARequest(HttpMethod.HEAD);
    }

    @Test
    public void itRejectsARequestOPTIONS() throws Exception {
        itRejectsARequest(HttpMethod.OPTIONS);
    }

    @Test
    public void itRejectsARequestPATCH() throws Exception {
        itRejectsARequest(HttpMethod.PATCH);
    }

    @Test
    public void itRejectsARequestPUT() throws Exception {
        itRejectsARequest(HttpMethod.PUT);
    }

    @Test
    public void itRejectsARequestTRACE() throws Exception {
        itRejectsARequest(HttpMethod.TRACE);
    }

    @Test
    public void itRejectsARequestGET() throws Exception {
        itRejectsARequest(HttpMethod.GET);
    }

    private void itRejectsARequest(final HttpMethod method) throws Exception {
        when(request.getMethod()).thenReturn(HttpMethod.GET);
        ArgumentCaptor<FullHttpResponse> responseCaptor = ArgumentCaptor.forClass(FullHttpResponse.class);

        this.underTest.channelRead(ctx, request);

        verify(request).getMethod();
        verify(ctx).writeAndFlush(responseCaptor.capture());
        verifyNoMoreInteractions(ctx, request, requestContent, requestHeaders);

        final FullHttpResponse actual = responseCaptor.getValue();
        assertEquals(405, actual.getStatus().code());
        assertEquals("", actual.content().toString(StandardCharsets.UTF_8));
    }

}
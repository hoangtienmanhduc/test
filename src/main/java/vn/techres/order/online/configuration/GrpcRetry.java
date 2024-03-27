package vn.techres.order.online.configuration;

import io.grpc.*;

import javax.annotation.Nullable;

public class GrpcRetry implements ClientInterceptor {
    private final int maxRetries;

    public GrpcRetry(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
                                                               CallOptions callOptions, Channel next) {
        return new RetryingClientCall<>(next.newCall(method, callOptions), maxRetries);
    }
    public static class RetryingClientCall<ReqT, RespT> extends ClientCall<ReqT, RespT> {
        private final ClientCall<ReqT, RespT> delegate;
        private final int maxRetries;
        private int attemptedRetries = 0;

        public RetryingClientCall(ClientCall<ReqT, RespT> delegate, int maxRetries) {
            this.delegate = delegate;
            this.maxRetries = maxRetries;
        }

        @Override
        public void start(Listener<RespT> listener, Metadata metadata) {

            delegate.start(new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(listener) {
                @Override
                public void onClose(Status status, Metadata trailers) {
                    if (status.isOk() || attemptedRetries >= maxRetries) {
                        super.onClose(status, trailers);
                    } else {
                        attemptedRetries++;
                        delegate.start(this, metadata);
                    }
                }
            }, metadata);
        }

        @Override
        public void request(int i) {
            delegate.request(i);
        }

        @Override
        public void cancel(@Nullable String s, @Nullable Throwable throwable) {
            delegate.cancel(s, throwable);
        }

        @Override
        public void halfClose() {
            delegate.halfClose();
        }

        @Override
        public void sendMessage(ReqT reqT) {
            delegate.sendMessage(reqT);
        }

    }

}


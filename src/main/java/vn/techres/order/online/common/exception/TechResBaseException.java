package vn.techres.order.online.common.exception;

public class TechResBaseException extends Exception {
    static final long serialVersionUID = -3387516993124229948L;

    public TechResBaseException(String errorMessage) {
        super(errorMessage);
    }
}

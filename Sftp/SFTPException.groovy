class SFTPException extends IOException {
    SFTPException() {
        super() 
    }

    SFTPException(String message) {
        super(message)
    }

    SFTPException(String message, Throwable cause) {
        super(message, cause)
    }

    SFTPException(Throwable cause) {
        super(cause)
    }
}

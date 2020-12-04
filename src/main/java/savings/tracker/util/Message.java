package savings.tracker.util;

public class Message {
  private int code;
  private String message;

  public Message() {

  }

  /**
   * Construct message object from input.
   * 
   * @param code2    the info code
   * @param message2 the info message
   */
  public Message(int code2, String message2) {
    code = code2;
    message = message2;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}

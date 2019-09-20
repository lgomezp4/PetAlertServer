package cat.proven.petAlert.restful;

import com.google.gson.annotations.Expose;

/**
 * ADT RequestResult
 *
 * @author Pet Alert
 */
public class RequestResult {

    /* ATTRIBUTES */
    @Expose
    private Object data;
    @Expose
    private int resultCode;

    /* CONSTRUCTORS */
    public RequestResult(Object data, int resultCode) {
        this.data = data;
        this.resultCode = resultCode;
    }

    /* GETTERS AND SETTERS */
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RequestResult{");
        sb.append("data=").append(data);
        sb.append(", resultCode=").append(resultCode);
        sb.append('}');
        return sb.toString();
    }
}

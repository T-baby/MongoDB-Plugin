import com.cybermkd.constraints.Chinese;
import com.cybermkd.kit.MongoValidate;
import org.hibernate.validator.constraints.SafeHtml;

/**
 * 创建人:T-baby
 * 创建日期: 16/7/5
 * 文件描述:
 */
public class AccountBean extends MongoValidate{

    @Chinese(value = true)
    private String username;
    @SafeHtml
    private String password;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

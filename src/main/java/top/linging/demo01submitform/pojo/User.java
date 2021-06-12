package top.linging.demo01submitform.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Linging
 * @version 1.0.0
 * @since 1.0
 */
@Data
public class User implements Serializable {

    private String username;

    private String password;

    private String key;
}

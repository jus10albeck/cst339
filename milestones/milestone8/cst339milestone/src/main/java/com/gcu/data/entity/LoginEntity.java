package com.gcu.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("login")
public class LoginEntity
{
    @Id
    private Long id;

    private String email;

    private String password;

    @Column("username_enc")
    private byte[] usernameEnc;

    @Column("username_hash")
    private byte[] usernameHash;

    public LoginEntity() {}

    public LoginEntity(Long id, String email, String password) 
    {
        this.setId(id);
        this.setEmail(email);
        this.setPassword(password);
    }

    /**
     * getters
     * @return
     */
    public Long getId() 
    { 
        return id; 
    }
    /**
     * setters
     * @param id
     */
    public void setId(Long id) 
    { 
        this.id = id; 
    }

    /**
     * getters
     * @return
     */
    public String getEmail() 
    { 
        return email; 
    }
    /**
     * setters
     * @param id
     */
    public void setEmail(String email) 
    { 
        this.email = email; 
    }

    /**
     * getters
     * @return
     */
    public String getPassword() 
    { 
        return password; 
    }
    /**
     * setters
     * @param id
     */
    public void setPassword(String password) 
    { 
        this.password = password; 
    }

    /**
     * getters
     * @return
     */
    public byte[] getUsernameEnc() 
    { 
        return usernameEnc; 
    }
    /**
     * setters
     * @param id
     */
    public void setUsernameEnc(byte[] usernameEnc) 
    { 
        this.usernameEnc = usernameEnc; 
    }

    /**
     * getters
     * @return
     */
    public byte[] getUsernameHash() 
    { 
        return usernameHash; 
    }
    /**
     * setters
     * @param id
     */
    public void setUsernameHash(byte[] usernameHash) 
    { 
        this.usernameHash = usernameHash; 
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xmppclient;

/**
 *
 * @author Lee
 */
public class Connection 
{
    private String username;
    private String name;
    private String resource;
    private String host;
    private String port;

    public Connection(String name, String username, String resource, String host, String port)
    {
        this.username = username;
        this.name = name;
        this.resource = resource;
        this.host = host;
        this.port = port;
    }

    public Connection()
    {
        name = "New connection";
        resource = "Home";
        port = "5222";
    }
    
    @Override
    public String toString()
    {
        return name;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPort()
    {
        return port;
    }

    public void setPort(String port)
    {
        this.port = port;
    }

    public String getResource()
    {
        return resource;
    }

    public void setResource(String resource)
    {
        this.resource = resource;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
}

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

    /**
     * Represents a stored connection to the server
     * @param name The name of the connection
     * @param username The username
     * @param resource The resource
     * @param host The address of the server
     * @param port The port to use
     */
    public Connection(String name, String username, String resource, String host, String port)
    {
        this.username = username;
        this.name = name;
        this.resource = resource;
        this.host = host;
        this.port = port;
    }

    /**
     * A default connection without username, password or host
     */
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

    /**
     * Gets the host of this connection
     * @return The host
     */
    public String getHost()
    {
        return host;
    }

    /**
     * Sets the host of this connection
     * @param host The host
     */
    public void setHost(String host)
    {
        this.host = host;
    }

    /**
     * Gets the name of this connection
     * @return Name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of this connection
     * @param name The name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Gets the port of this connection
     * @return Port
     */
    public String getPort()
    {
        return port;
    }
    
    public int getPortAsInt()
    {
        return Integer.parseInt(port);
    }

    /**
     * Sets the port of this connection
     * @param port The port
     */
    public void setPort(String port)
    {
        this.port = port;
    }

    /**
     * Gets the resource of this connection
     * @return The resource
     */
    public String getResource()
    {
        return resource;
    }

    /**
     * Sets the resource of this connection
     * @param resource The resource
     */
    public void setResource(String resource)
    {
        this.resource = resource;
    }

    /**
     * Gets the username for this connection
     * @return Username
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Sets the username for this connection
     * @param username The username
     */
    public void setUsername(String username)
    {
        this.username = username;
    }
}

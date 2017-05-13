/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jarno
 */
@XmlRootElement
public class Comment implements Serializable, Comparable<Comment> {

    protected String body;
    protected String creator;
    protected String signed;
    protected LocalDateTime creationtime;
    protected int id; //used to search for the comment/review on back end when responding to it.
    protected LinkedList<Comment> comments = new LinkedList<Comment>();

    public Comment(String body, String signed, String creator) {
        this.body = body;
        this.creator = creator;
        this.signed = signed;
        this.creationtime = LocalDateTime.now();
        this.id = hashCode();
    }

    public Comment() {
    }

    public boolean respond(Comment response,int cmntId) {
        if(this.id==cmntId){
            comments.add(response);
            return true;
        }
        for(Comment c:comments){
            if(c.respond(response, cmntId)){
                return true;
            }
        }
        return false;
    }

    public boolean removeComment(int id) {
        for (Comment cmnt : comments) {
            if (cmnt.id == id) {
                comments.remove(cmnt);
                return true;
            } else {
                if (cmnt.removeComment(id)) {
                    return true;
                }
            }
        }
        return false;
    }

    @XmlElement
    public String getBody() {
        return this.body;
    }
    
    public void setBody(String newbody){
        this.body=newbody;
    }

    @XmlElement
    public String getCreator() {
        return this.creator;
    }

    //final version will retrieve the up-to-date  full name from UserManager singleton
    @XmlElement
    public String getSigned() {
        return this.signed;
    }

    @XmlElement
    public String getCreationtime() {
        return this.creationtime.toString();
    }

    @XmlElement
    public List<Comment> getComments() {
        return this.comments;
    }

    @XmlElement
    public int getId() {
        return this.id;
    }
    
    /* User addition in UserResource checks to make sure no two users have the same username. */
    @Override
    public int hashCode() {
        int hash = 1 + 13 * this.creator.hashCode() + 7 * this.creationtime.toString().hashCode() +11*this.body.hashCode();
        return hash;
    }

    /*
    If in an ordered container, comments (and reviews) are stored 'latest first'.
    */
    @Override
    public int compareTo(Comment other) {
        int time=other.creationtime.compareTo(this.creationtime);
        if(time==0){
            return this.hashCode()-other.hashCode();
        }
        else{
        return other.creationtime.compareTo(this.creationtime);
        }
    }
}

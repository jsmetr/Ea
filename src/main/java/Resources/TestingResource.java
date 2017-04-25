/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resources;

/**
 *
 * @author Jarno
 */


import DataClasses.*;
import java.util.ArrayList;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/Testing")
public class TestingResource {
    private UriInfo context;
    
    @Path("/Comments/{amount}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Comment> grabComments(@PathParam("amount") int amount){
        List<Comment> comments = new LinkedList<Comment>();
        String junk ="This text is filler. This text is filler. This text is filler. This text is filler. This text is filler. This text is filler.";
        Comment cmnt = new Comment(junk,"Junkmaker","username");
        int i=0;
        while(i<amount){
            comments.add(cmnt);
            i++;
        }
        return comments;
    }
    
    @Path("/Comments")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Comment> grabsomeComments(){
        List<Comment> comments = new LinkedList<Comment>();
        String junk ="This text is filler. This text is filler. This text is filler. This text is filler. This text is filler. This text is filler.";
        Comment cmnt = new Comment(junk,"Junkmaker","username");
        int i=0;
        while(i<5){
            comments.add(cmnt);
            i++;
        }
        return comments;
    }
    
    @Path("/Review")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public ReviewShell grabReview(){
        String junk1 ="This text is filler. This text is filler. This text is filler. This text is filler. This text is filler. This text is filler.";
        String junk2 ="This is a filler review. This is a filler review. This is a filler review. This is a filler review. This is a filler review.";
        String junk3 ="This comment on comment is filler. This comment on comment is filler. This comment on comment is filler. This comment on comment is filler. ";
        String junk4 ="This is another comment on comment as well as filler. This is another comment on comment as well as filler. This is another comment on comment as well as filler.";
        String junk5 ="This is another direct response. This is another direct response. This is another direct response. ";
        Review rvw = new Review(5,junk2,"ProReviewer","username1");
        ReviewShell shell = new ReviewShell(rvw);
        Comment cmnt1 = new Comment(junk1,"PlainUser","username2");
        Comment cmnt2 = new Comment(junk3,"ProReviewer","username1");
        Comment cmnt3 = new Comment(junk4,"Junkmaker","username3");
        Comment cmnt4 = new Comment(junk5,"Junkmaker","username3");
        cmnt1.respond(cmnt2);
        cmnt1.respond(cmnt3);
        shell.commentOn(cmnt1);
        shell.commentOn(cmnt4);
        return shell;
    }
    
    @Path("/Reviews")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Review> grabReviews(){
        ArrayList<Review> reviews = new ArrayList<Review>();
        String junk1 ="This text is filler. This text is filler. This text is filler. This text is filler. This text is filler. This text is filler.";
        String junk2 ="This is a filler review. This is a filler review. This is a filler review. This is a filler review. This is a filler review.";
        String junk3 ="This is another filler review. This is another filler review. This is another filler review. This is another filler review.";
        String junk4 ="This is yet another filler review. This is yet another filler review. This is yet another filler review. This is yet another filler review. ";
        Review rvw1 = new Review(5,junk1,"ProReviewer","username1");
        Review rvw2 = new Review(5,junk2,"Junkmaker","username3");
        Review rvw3 = new Review(5,junk3,"NotAHacker","username4");
        Review rvw4 = new Review(5,junk4,"PlainUser","username2");
        reviews.add(rvw1);
        reviews.add(rvw2);
        reviews.add(rvw3);
        reviews.add(rvw4);
        return reviews;
    }
    
    @Path("/Response")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String respond(){
        return "This is a response.";
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resources;

import DataClasses.*;
import DataManagement.DeviceManager;
import DataManagement.LoginManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author Jarno
 */
@Path("/Devices")
public class DeviceResource {

    LoginManager LogMan = LoginManager.getInstance();
    DeviceManager DevMan = DeviceManager.getInstance();


    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String herp(@PathParam("sessionId") String sessionId) {
        if (LogMan.CheckSession(sessionId)) {

        }
        return "SESSION EXPIRED";
    }
    
    @Path("/Devicetypes")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<DeviceType> getTypes() {
        return DevMan.getTypes();
    }
    
    /*
    Grabs all devices owned by a customer.
    */
    @Path("/Devices/{customer}/{sessionId}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<Device> getCustDev(@PathParam("sessionId") String sessionId,@PathParam("customer") String customer){
        if (LogMan.CheckSession(sessionId)) {
            Set<Device> devices = new HashSet<Device>();
            for(Device d: DevMan.getDevices()){
                if(d.getOwner().equals(customer)){
                    devices.add(d);
                }
            }
            return devices;
        }
        return null;
    }

    @Path("/AddDevice/{type}/{name}/{owner}/{manufacturer}/{model}/{sessionId}")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String addDevice(@PathParam("sessionId") String sessionId, @PathParam("name") String name, @PathParam("type") String type, 
            @PathParam("owner") String owner, @PathParam("model") String model, @PathParam("manufacturer") String manufacturer) {
        if (LogMan.CheckSession(sessionId)) {
            if(LogMan.getBySesId(sessionId).getAccess() < 1){
                return "ACCESS DENIED";
            }
            DeviceType devtype = DevMan.getTypeByName(type);
            Device device = new Device(owner,name,devtype,manufacturer,model);//String owner, String name, DeviceType type,String manufacturer,String model
            boolean added=DevMan.addDevice(device);
            if(added){
                return "DEVICE TYPE ADDED";
            }
            return "FAILURE";
        }
        return "SESSION EXPIRED";
    }

    @Path("/AddType/{name}/{data}/{sessionId}")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String addType(@PathParam("sessionId") String sessionId, @PathParam("name") String typename, @PathParam("data") String typedata) {
        if (LogMan.CheckSession(sessionId)) {
            if(LogMan.getBySesId(sessionId).getAccess() < 1){
                return "ACCESS DENIED";
            }
            DeviceType newtype = new DeviceType(typename,typedata);
            boolean added = DevMan.addDeviceType(newtype);
            if(added){
                return "DEVICE TYPE ADDED";
            }
            return "FAILURE";
        }
        return "SESSION EXPIRED";
    }

    @Path("/CreateAssignment/{deviceid}/{deadline}/{customer}/{technician}/{sessionId}")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String createAssignment(@PathParam("sessionId") String sessionId,@PathParam("sessionId") String deviceid,@PathParam("sessionId") String deadline,@PathParam("sessionId") String customer,@PathParam("sessionId") String technician) {
        if (LogMan.CheckSession(sessionId)) {
            if(LogMan.getBySesId(sessionId).getAccess() < 1){
                return "ACCESS DENIED";
            }
            String clerk=LogMan.getBySesId(sessionId).getUserName();
            Device item=DevMan.getDeviceById(Integer.parseInt(deviceid));
            Assignment created=new Assignment(item,deadline,customer,clerk,technician);
            boolean added = DevMan.addAssignment(created);
            if(added){
                return "ASSIGNMENT CREATED";
            }
            return "FAILURE";
        }
        return "SESSION EXPIRED";
    }

    /*
    Stores a comment responding directly to a review.
     */
    @Path("/postReview/{assignid}/{title}/{body}/{rating}/{sessionId}")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String postReview(@PathParam("sessionId") String sessionId, @PathParam("assignid") String assignid, @PathParam("title") String title, @PathParam("rating") String rt, @PathParam("body") String body) {
        if (LogMan.CheckSession(sessionId)) {
            Person u = LogMan.getBySesId(sessionId);
            ReviewShell shell;
            int rating = Integer.parseInt(rt);
            int id = Integer.parseInt(assignid);
            Set<Assignment> assignments = DevMan.getAssignments();
            for (Assignment a : assignments) {
                if (a.getId()==id) {
                    if(u.getUserName()!=a.getCustomer()){
                        return "You are not the customer of this assignment.";
                    }
                    String signed=LogMan.getBySesId(sessionId).getFirstName()+""+LogMan.getBySesId(sessionId).getLastName();
                    Review newreview = new Review(title,rating,body,LogMan.getBySesId(sessionId).getUserName(),signed);
                    ReviewShell newshell = new ReviewShell(newreview);
                    a.addReview(newshell);
                    DevMan.save();
                    return "Assignment found and review added.";

                }
            }
            return "Assignment not found.";
        }
        return "SESSION EXPIRED";
    }

    /*
    Stores a comment responding directly to a review.
     */
    @Path("/respondToReview/{body}/{reviewId}/{sessionId}")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String respondToReview(@PathParam("sessionId") String sessionId, @PathParam("reviewId") String reviewId, @PathParam("body") String body) {
        if (LogMan.CheckSession(sessionId)) {
            ReviewShell shell;
            int rewId = Integer.parseInt(reviewId);
            Set<Assignment> assignments = DevMan.getAssignments();
            for (Assignment a : assignments) {
                if (a.getReview() != null) {
                    if (a.getReview().getReviewId() == rewId) {
                        shell = a.getReview();
                        Person u = LogMan.getBySesId(sessionId);
                        Comment cmnt = new Comment(body, u.getFirstName() + " " + u.getLastName(), u.getUserName());
                        shell.commentOn(cmnt);
                        DevMan.save();
                        return "Comment found and responded to.";
                    }
                }
            }
            return "Comment not found.";
        }
        return "SESSION EXPIRED";
    }

    /*
    Stores a comment responding to another existing comment.
     */
    @Path("/respondToComment/{body}/{reviewId}/{commentId}/{sessionId}")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public String respondToComment(@PathParam("sessionId") String sessionId, @PathParam("reviewId") String reviewId, @PathParam("commentId") String commentId, @PathParam("body") String body) {
        if (LogMan.CheckSession(sessionId)) {
            ReviewShell shell;
            int rewId = Integer.parseInt(reviewId);
            int cmntId = Integer.parseInt(commentId);
            Set<Assignment> assignments = DevMan.getAssignments();
            for (Assignment a : assignments) {
                if (a.getReview() != null) {
                    if (a.getReview().getReviewId() == rewId) {
                        shell = a.getReview();
                        Person u = LogMan.getBySesId(sessionId);
                        Comment cmnt = new Comment(body, u.getFirstName() + " " + u.getLastName(), u.getUserName());
                        if (shell.respond(cmnt, cmntId) == true) {
                            DevMan.save();
                            return "Comment found and responded to.";
                        }
                    }
                }
            }
            return "Comment not found.";
        }
        return "SESSION EXPIRED";
    }

    /*
    Returns a desired amount of recent reviews for any type of device.
    Not controlled by sessionId since this is supposed to be open to all browsers.
     */
    @Path("/getReviews/Latest/{amount}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<Review> getLatest(@PathParam("amount") String amnt) {
        Set<Review> reviews = new TreeSet<Review>();
        int amount = Integer.parseInt(amnt);
        int count = 0;
        for (Assignment a : DevMan.getAssignments()) {
            if (a.getReview() != null) {
                reviews.add(a.getReview().getReview());
                count++;
                if (!(count < amount)) {
                    return reviews;
                }
            }
        }
        return reviews; //even if there are not enough to fill the quota, whatever was found is returned.
    }

    /*
    Returns a desired amount of recent reviews for the selected type of device.
    Not controlled by sessionId since this is supposed to be open to all browsers.
     */
    @Path("/getReviews/ByType/{type}/{amount}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Set<Review> getByType(@PathParam("amount") String amnt, @PathParam("type") String type) {
        Set<Review> reviews = new TreeSet<Review>();
        int amount = Integer.parseInt(amnt);
        int count = 0;
        for (Assignment a : DevMan.getAssignments()) {
            if (a.getReview() != null && a.getDevice().getType().getName().equals(type)) {
                reviews.add(a.getReview().getReview());
                count++;
                if (!(count < amount)) {
                    return reviews;
                }
            }
        }
        return reviews; //even if there are not enough to fill the quota, whatever was found is returned.
    }
}

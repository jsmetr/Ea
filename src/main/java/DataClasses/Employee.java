/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jarno
 */
@XmlRootElement
public class Employee extends Person implements Serializable{
    private Set<RepairSkill> skills; //Technician skills, you either have it or you don't and the skill level is approximated as an integer (0-10)
    
    public Employee(String fname, String lname, String uname, String psw, String email, String phone,int access,String role){
        super( fname,  lname,  uname,  psw,  email,  phone, access);
        this.role=role;
        this.skills =new HashSet<RepairSkill>();
    }
    
    public Employee(){}
    
    /*
    Handles both adding and updating skills, as original implementation was as a Map<String, Integer> until XML support was found wanting.
    */
    public void changeSkill(String typename, int lvl){
        RepairSkill skill = new RepairSkill(typename,lvl);
        if(!skills.add(skill)){
            for(RepairSkill s:skills){
                if(s.getDevicetype().equals(typename)){
                    s.setLevel(lvl);
                    break;
                }
            }
        }
    }
    
    @XmlElement
    public Set<RepairSkill> getSkills(){
        return this.skills;
    }
}

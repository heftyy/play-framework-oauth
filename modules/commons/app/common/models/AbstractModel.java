package common.models;

import play.db.jpa.JPA;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractModel extends JsonSerializable {

    public abstract Long getId();

    public void save() {
        if(getId() == null || getId() == 0) JPA.em().persist(this);
        else JPA.em().merge(this);
    }
}

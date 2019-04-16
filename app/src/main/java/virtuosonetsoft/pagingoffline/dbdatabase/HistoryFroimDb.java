package virtuosonetsoft.pagingoffline.dbdatabase;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

//public class HistoryFroimDb {
//}
@Entity(tableName = "history")

public class HistoryFroimDb {

    @PrimaryKey
    @SerializedName("id")
    public long id;

    @SerializedName("equipements_types_id")
    private String equipements_types_id;
    @SerializedName("booking_from")
    private String booking_from;
    @SerializedName("partner_name")
    private String partner_name;
    @SerializedName("equipement_id")
    private String equipement_id;
    @SerializedName("equipements_types_name")
    private String equipements_types_name;
    @SerializedName("booking_to")
    private String booking_to;
    @SerializedName("booking_to_time")
    private String booking_to_time;
    @SerializedName("booking_quantity")
    private String booking_quantity;
    @SerializedName("equipements_types_image")
    private String equipements_types_image;
    @SerializedName("booking_type")
    private String booking_type;

    @SerializedName("total_booking_amount")
    private String total_booking_amount;
    @SerializedName("booking_from_time")
    private String booking_from_time;
    @SerializedName("status")
    private String status;

    public String getEquipements_types_id ()
    {
        return equipements_types_id;
    }

    public void setEquipements_types_id (String equipements_types_id)
    {
        this.equipements_types_id = equipements_types_id;
    }

    public String getBooking_from ()
    {
        return booking_from;
    }

    public void setBooking_from (String booking_from)
    {
        this.booking_from = booking_from;
    }

    public String getPartner_name ()
    {
        return partner_name;
    }

    public void setPartner_name (String partner_name)
    {
        this.partner_name = partner_name;
    }

    public String getEquipement_id ()
    {
        return equipement_id;
    }

    public void setEquipement_id (String equipement_id)
    {
        this.equipement_id = equipement_id;
    }

    public String getEquipements_types_name ()
    {
        return equipements_types_name;
    }

    public void setEquipements_types_name (String equipements_types_name)
    {
        this.equipements_types_name = equipements_types_name;
    }

    public String getBooking_to ()
    {
        return booking_to;
    }

    public void setBooking_to (String booking_to)
    {
        this.booking_to = booking_to;
    }

    public String getBooking_to_time ()
    {
        return booking_to_time;
    }

    public void setBooking_to_time (String booking_to_time)
    {
        this.booking_to_time = booking_to_time;
    }

    public String getBooking_quantity ()
    {
        return booking_quantity;
    }

    public void setBooking_quantity (String booking_quantity)
    {
        this.booking_quantity = booking_quantity;
    }

    public String getEquipements_types_image ()
    {
        return equipements_types_image;
    }

    public void setEquipements_types_image (String equipements_types_image)
    {
        this.equipements_types_image = equipements_types_image;
    }

    public String getBooking_type ()
    {
        return booking_type;
    }

    public void setBooking_type (String booking_type)
    {
        this.booking_type = booking_type;
    }

    public String getId ()
    {
        return String.valueOf(id);
    }

    public void setId (String id)
    {
        this.id = Long.parseLong(id);
    }

    public String getTotal_booking_amount ()
    {
        return total_booking_amount;
    }

    public void setTotal_booking_amount (String total_booking_amount)
    {
        this.total_booking_amount = total_booking_amount;
    }

    public String getBooking_from_time ()
    {
        return booking_from_time;
    }

    public void setBooking_from_time (String booking_from_time)
    {
        this.booking_from_time = booking_from_time;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [equipements_types_id = "+equipements_types_id+", booking_from = "+booking_from+", partner_name = "+partner_name+", equipement_id = "+equipement_id+", equipements_types_name = "+equipements_types_name+", booking_to = "+booking_to+", booking_to_time = "+booking_to_time+", booking_quantity = "+booking_quantity+", equipements_types_image = "+equipements_types_image+", booking_type = "+booking_type+", id = "+id+", total_booking_amount = "+total_booking_amount+", booking_from_time = "+booking_from_time+", status = "+status+"]";
    }
}

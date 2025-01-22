package org.swe.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Event {
    private final int id;
    private final String title;
    private final String description;
    private final Date date;
    private final int ticketsAvailable;
    private final double ticketPrice;
    private final List<Staff> staff;   
    private final List<Admin> admins;  

    private Event(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.date = builder.date;
        this.ticketsAvailable = builder.ticketsAvailable;
        this.ticketPrice = builder.ticketPrice;
        this.staff = builder.staff;
        this.admins = builder.admins;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public int getTicketsAvailable() {
        return ticketsAvailable;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    // Getter per le liste
    public List<Staff> getStaff() {
        return staff;
    }

    public List<Admin> getAdmins() {
        return admins;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", ticketsAvailable=" + ticketsAvailable +
                ", ticketPrice=" + ticketPrice +
                ", staff=" + staff +
                ", admins=" + admins +
                '}';
    }


    public static class Builder {
        private int id;
        private String title;
        private String description;
        private Date date;
        private int ticketsAvailable;
        private double ticketPrice;
        private List<Staff> staff = new ArrayList<>();
        private List<Admin> admins = new ArrayList<>();

        public Builder setId(int id) {
            if (id <= 0) {
                throw new IllegalArgumentException("ID must be greater than 0.");
            }
            this.id = id;
            return this;
        }

        public Builder setTitle(String title) {
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Title cannot be null or empty.");
            }
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            if (description == null || description.trim().isEmpty()) {
                throw new IllegalArgumentException("Description cannot be null or empty.");
            }
            this.description = description;
            return this;
        }

        public Builder setDate(Date date) {
            if (date == null || date.before(new Date())) {
                throw new IllegalArgumentException("Date must not be null and must be in the future.");
            }
            this.date = date;
            return this;
        }

        public Builder setTicketsAvailable(int ticketsAvailable) {
            if (ticketsAvailable < 0) {
                throw new IllegalArgumentException("Tickets available must be 0 or greater.");
            }
            this.ticketsAvailable = ticketsAvailable;
            return this;
        }

        public Builder setTicketPrice(double ticketPrice) {
            if (ticketPrice < 0) {
                throw new IllegalArgumentException("Ticket price must be 0 or greater.");
            }
            this.ticketPrice = ticketPrice;
            return this;
        }

        public Builder setStaff(List<Staff> staff) {
            if (staff == null) {
                throw new IllegalArgumentException("Staff list cannot be null.");
            }
            this.staff = staff;
            return this;
        }

        public Builder setAdmins(List<Admin> admins) {
            if (admins == null) {
                throw new IllegalArgumentException("Admin list cannot be null.");
            }
            this.admins = admins;
            return this;
        }

        public Event build() {
            Objects.requireNonNull(title, "Title must not be null.");
            Objects.requireNonNull(description, "Description must not be null.");
            Objects.requireNonNull(date, "Date must not be null.");
            return new Event(this);
        }
    }
}
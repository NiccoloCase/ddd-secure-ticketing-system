package org.swe.model;

public final class VerificationSessionResult {
     private Guest guest;
     private VerifySessionStatus status;
     private int validatedTickets; 

     public VerificationSessionResult(Guest guest, VerifySessionStatus status, int validatedTickets) {
          this.guest = guest;
          this.status = status;
          this.validatedTickets = validatedTickets;
     }

     public Guest getGuest() {
          return guest;
     }
     
     public VerifySessionStatus getStatus() {
          return status;
     }

     public int getValidatedTickets() {
          return validatedTickets;
     }
}

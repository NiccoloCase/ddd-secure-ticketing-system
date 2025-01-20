package org.swe.model;

public final class VerificationSessionResult {
     private final String guestIdentity;
     private final VerifySessionStatus status;
     private final int validatedTickets;

     public VerificationSessionResult(String guestIdentity, VerifySessionStatus status, int validatedTickets) {
          this.guestIdentity = guestIdentity;
          this.status = status;
          this.validatedTickets = validatedTickets;
     }

     public String getGuestIdentity() {
          return guestIdentity;
     }
     
     public VerifySessionStatus getStatus() {
          return status;
     }

     public int getValidatedTickets() {
          return validatedTickets;
     }
}

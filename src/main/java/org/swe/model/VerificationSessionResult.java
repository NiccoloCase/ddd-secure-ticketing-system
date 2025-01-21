package org.swe.model;

public final class VerificationSessionResult {
     private final String userIdentity;
     private final VerifySessionStatus status;
     private final int validatedTickets;

     public VerificationSessionResult(String guestIdentity, VerifySessionStatus status, int validatedTickets) {
          this.userIdentity = guestIdentity;
          this.status = status;
          this.validatedTickets = validatedTickets;
     }

     public String getUserIdentity() {
          return userIdentity;
     }
     
     public VerifySessionStatus getStatus() {
          return status;
     }

     public int getValidatedTickets() {
          return validatedTickets;
     }
}

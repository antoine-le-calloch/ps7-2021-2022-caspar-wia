import http from "@/http-common";
import FBCA from "@/model/FBCA";

class FBCAService {

  /**
   * get the valid state of the latest FBCA with the given passport number
   * @param passportNumber The passport number of the FBCA
   * @returns if the latest FBCA with the given passport number is valid or not.
   */
  isFBCAValid(passportNumber: string): Promise<Boolean> {
    return http.get("/fbca/isValid/passport/"+passportNumber);
  }

  /**
   * Get the latest FBCA with the given passport number
     * @param passportNumber The passport number of the FBCA
     * @returns the latest FBCA with the given passport number if the caller has the right to do it. Else an error is returned. If the passport number doesn't exist,
     * an error is returned
   */
  getFBCAByPassportNumber(passportNumber: string): Promise<any> {
    return http.get("/fbca/passport/"+passportNumber);
  }

  /**
     * Get all the FBCA with the given last name and first name
     * @param lastName The last name wanted
     * @param firstName (Optional) The first name wanted
     * @returns - If only the last name is here, a list of FBCAs is returned. The FBCAs are WAITING_FOR_APPROVAL and after today's date.
     * If the last name doesn't exist or the caller doesn't have the right, an error is returned.
     * - If only the first name is here, an error is returned.
     * - If the last and first name are here, the latest FBCA with this information is returned. If the couple doesn't exist or the caller doesn't have the right,
     * an error is returned
     */
  getFBCAByFirstNameAndLastName(firstName: string, lastName: string): Promise<any> {
    const query = "?lastName=" + lastName + (firstName ? '&firstName=' + firstName : '');
    return http.get("/fbca/firstNameAndLastName" + query);
  }

  /**
     * Add the given FBCA in the database
     * @param fbca The FBCA to add
     * @returns The saved FBCA. If the given FBCA doesn't respect the Schema, an error is returned
     */
  addFBCA(fbca: FBCA): Promise<any> {
    return http.post("/fbca", fbca);
  }

  /**
     * Update the FBCA with the given id
     * @param id The id of the FBCA to update
     * @param fbca The FBCA to update
     * @returns The FBCA updated. If the id doesn't exit, an error is returned. If the caller doesn't have the right, an error is returned
     */
  updateFBCA(id: number, fbca: FBCA): Promise<any> {
    return http.put("/fbca/update/"+id, fbca);
  }
}

export default new FBCAService();

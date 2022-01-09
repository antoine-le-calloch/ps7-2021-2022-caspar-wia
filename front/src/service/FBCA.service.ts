import http from "@/http-common";
import FBCA from "@/model/FBCA";

class FBCAService {
  getFBCAByPassportNumber(passportNumber: string): Promise<any> {
    return http.get("/fbca/passport/"+passportNumber);
  }

  addFBCA(fbca: FBCA): Promise<any> {
    return http.post("/fbca/add", fbca);
  }
}

export default new FBCAService();

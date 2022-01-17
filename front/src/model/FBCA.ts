import { Freight } from "./Freight";

export default interface FBCA {
    id?: number;
    firstName: string;
    lastName:string;
    email: string;
    phone: string;
    passportNumber: string;
    expirationDate: string;
    crossingReason: string;
    transportType: string;
    valid: boolean;
    state: string;
    declinedReason?: string;
    freights?: Freight[];
}
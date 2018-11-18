import {ItemSpecification} from "./item_specification";
import {User} from "../../registration/user";

export interface LoanableItem{
  id: Number;
  spec: ItemSpecification;
  type: string;
  available: boolean;
  client: User;
}

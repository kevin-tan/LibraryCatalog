import {ItemSpecification} from "./item-specification/item_specification";
import {User} from "../../registration/user";

export interface LoanableItem {
  id: number;
  spec: ItemSpecification;
  type: string;
  available: boolean;
  client: User;
}

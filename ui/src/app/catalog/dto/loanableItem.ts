import {ItemSpecification} from "./item_specification";

export interface LoanableItem{
  id: string;
  spec: ItemSpecification;
  type: string;
  available: boolean;
  client: user;
}

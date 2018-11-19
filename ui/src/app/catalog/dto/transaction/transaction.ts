import {User} from "../../../registration/user";
import {LoanableItem} from "../loanableItem";

export interface Transaction{
  id: Number;
  lonableItem: LoanableItem;
  client: User;
  transactionDate: Date;
}

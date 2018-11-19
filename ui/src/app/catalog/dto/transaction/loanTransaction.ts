import {Transaction} from "./transaction";


export interface LoanTransaction extends Transaction {
  dueDate: Date;
}

import {ItemSpecification} from "../catalog/dto/item-specification/item_specification";


export interface Transaction {
  itemId: number;
  userId: number;

  loanableItem: {
    LoanableItem: {
      id: number;
      type: string;
        spec: ItemSpecification;
    }
  }

  client: {
    Client: {
      id: number;
      firstName: string;
      email: string;
    }
  }

  transactionDate: Date;
}

export interface ReturnTransaction extends Transaction {

}

export interface LoanTransaction extends Transaction {
  dueDate: Date;
}


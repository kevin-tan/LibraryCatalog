export class User {
  id: number;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  physicalAddress: string;

  constructor(
    id: number
  ) {
    this.id = id;
    this.email = null;
    this.password = null;
    this.firstName = null;
    this.lastName = null;
    this.phoneNumber = null;
    this.physicalAddress = null;
  }
}

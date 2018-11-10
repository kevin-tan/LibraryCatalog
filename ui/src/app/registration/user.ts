export class User {
  id: number;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  physicalAddress: string;

  constructor(
    id: number,
    email: string,
    password: string,
    firstName: string,
    lastName: string,
    phoneNumber: string,
    physicalAddress: string
  ) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phoneNumber = phoneNumber;
    this.physicalAddress = physicalAddress;
  }
}

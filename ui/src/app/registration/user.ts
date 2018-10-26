export class User {
  constructor(
    public userType: string,
    public id: string,
    public email: string,
    public password: string,
    public firstName: string,
    public lastName: string,
    public phoneNumber: string,
    public address: string
    ) {}
}

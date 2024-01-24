Use database_1;
Create table Customers (CId int primary Key auto_increment,CName varchar(25) NOT NULL , MobileNo int Unique NOT NULL, Address varchar(100) NULL,Balance int NULl);
Create table Purchase (PId int Primary Key auto_increment,CId int , PurchaseDate datetime , Cost int,Received_Amount int);
Create table Account (CId int Primary Key,PId int, CName varchar(25) NOT NULL,Balance int);
Create table PurchaseHistory(CId int,MobileNo int,SettlementDate datetime,Amount int);
Insert into Customers Values(default,"Ramu",1234567890,"abcdefghijk",0);
Insert into Purchase Values(default,1,sysdate(),1000,500);
Insert into Account Values(1,(Select Max(PId) From Purchase where Purchase.CID=1),"Ram",0);
Select * from customers;
Select * from Purchase;
Select sum(Cost),Sum(Received_Amount) as c from Purchase group by Purchase.CId=1;
Update Account set Balance = (
							Select sum(Cost) as c from Purchase where Purchase.CId=1
							)-(
							Select sum(Received_Amount) as received from Purchase where Purchase.CId=1
                            )
                            where CId=1;
Select * from Account; 

Select Received_Amount as a from Purchase where CId=1  order by PurchaseDate desc LIMIT 1;
#Balance ==0
Insert into PurchaseHistory Values(1,1234567890,sysdate(),(
															Select Received_Amount as a from Purchase 
                                                            where CId=1  order by PurchaseDate desc LIMIT 1));
Update Customers,Account Set Customers.Balance= (Select Balance From Account where Account.CId= 1) where Customers.CId=1;
Delete from Purchase where Purchase.CId=1 and (Select Balance from Account where Account.CId=1) =0 Order By PurchaseDate asc LIMIT 3;
Select * from PurchaseHistory;
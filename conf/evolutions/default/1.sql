# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table book (
  id                        bigint not null,
  name                      varchar(255),
  description               TEXT,
  library_id                bigint,
  constraint pk_book primary key (id))
;

create table library (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_library primary key (id))
;

create table patron (
  id                        bigint not null,
  name                      varchar(255),
  library_id                bigint,
  constraint pk_patron primary key (id))
;

create table transaction (
  id                        bigint not null,
  patron_id                 bigint,
  book_id                   bigint,
  checkout_at               timestamp,
  checkin_at                timestamp,
  days_before_late          integer,
  constraint pk_transaction primary key (id))
;

create sequence book_seq;

create sequence library_seq;

create sequence patron_seq;

create sequence transaction_seq;

alter table book add constraint fk_book_library_1 foreign key (library_id) references library (id) on delete restrict on update restrict;
create index ix_book_library_1 on book (library_id);
alter table patron add constraint fk_patron_library_2 foreign key (library_id) references library (id) on delete restrict on update restrict;
create index ix_patron_library_2 on patron (library_id);
alter table transaction add constraint fk_transaction_patron_3 foreign key (patron_id) references patron (id) on delete restrict on update restrict;
create index ix_transaction_patron_3 on transaction (patron_id);
alter table transaction add constraint fk_transaction_book_4 foreign key (book_id) references book (id) on delete restrict on update restrict;
create index ix_transaction_book_4 on transaction (book_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists book;

drop table if exists library;

drop table if exists patron;

drop table if exists transaction;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists book_seq;

drop sequence if exists library_seq;

drop sequence if exists patron_seq;

drop sequence if exists transaction_seq;


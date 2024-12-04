# msvc-transactions

msvc-transactions es una api de tipo Rest para la compañia BankInc que permite emitir a sus clientes una tarjeta débito o crédito para realizar compras en los comercios. En la actualidad soporta las siguientes funcionalidades:


	- Mensajeria tipo JSON para la comunicación con los clientes externos.
	- Creación de tarjetas.
	- Activación de tarjetas.
	- Recarga de saldo.
	- Consulta de saldo.
	- Bloqueo de tarjeta.
	- Transacción de Compra.
	- Consulta de Transacción.
	- Anulación de Compra.

## Transacciones financieras

msvc-transactions maneja la información recibida por el endpoint usando mensajeria en formato JSON, realiza validaciones de datos y posteriormente inserta en base de datos la información.

## Flujo Normal
	1- Se recibe la petición.
	2- Se realizan las validaciones necesarias según el endpoint.
	3- Se procesa la petición.
	4- Se actualiza información en base de datos.
	5- Se envía una respuesta. 

## Flujo de excepciónes
	 1- En caso de recibirse un request con formato invalido se responderá con HttpStatus 400 - BadRequest.
	 2- En caso de recibirse campos con formatos invalidos se responderá con HttpStatus 400 - BadRequest.
	 3- En caso de recibirse información no encontrada en base de datos se responderá con HttpStatus 404 - NotFound.

##Base de Datos

Este componente esta conectado a un BD relacional MySql con el nombre <code>msvc_transactions</code> para:

	- Almacenar las tarjetas creadas.
	- Consultar Tarjetas previamente creadas.
	- Consultar y actualizar estado de tarjetas.
	- Activar tarjetas.
	- Cargar y consultar saldos en tarjetas.
	- Insertar, consultar y anular transacciones de compras.
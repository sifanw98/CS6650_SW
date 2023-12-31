/*
 * Album Store API
 *
 * CS6650 Fall 2023
 *
 * API version: 1.0.0
 * Contact: i.gorton@northeasern.edu
 * Generated by: Swagger Codegen (https://github.com/swagger-api/swagger-codegen.git)
 */
 package swagger

 import (
	 "io"
	 "net/http"
	 "strings"
	 "github.com/gorilla/mux"
 )
 
 type Route struct {
	 Name        string
	 Method      string
	 Pattern     string
	 HandlerFunc http.HandlerFunc
 }
 
 type Routes []Route
 
 func NewRouter() *mux.Router {
	 router := mux.NewRouter().StrictSlash(true)
	 for _, route := range routes {
		 var handler http.Handler
		 handler = route.HandlerFunc
		 handler = Logger(handler, route.Name)
 
		 router.
			 Methods(route.Method).
			 Path(route.Pattern).
			 Name(route.Name).
			 Handler(handler)
	 }
 
	 return router
 }
 
 func Index(w http.ResponseWriter, r *http.Request) {
	 io.WriteString(w, "Hello, HTTP!\n")
 }
 
 var routes = Routes{
	 Route{
		 "Index",
		 "GET",
		 "/hello",
		 Index,
	 },
 
	 Route{
		 "GetAlbumByKey",
		 strings.ToUpper("Get"),
		 "/IGORTON/AlbumStore/1.0.0/albums/{albumID}",
		 GetAlbumByKey,
	 },
 
	 Route{
		 "NewAlbum",
		 strings.ToUpper("Post"),
		 "/IGORTON/AlbumStore/1.0.0/albums",
		 NewAlbum,
	 },
 }
 

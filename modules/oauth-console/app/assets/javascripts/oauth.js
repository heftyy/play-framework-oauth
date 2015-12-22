define(['libs/grid_filterable', 'moment'], function (GRID_FILTERABLE) {

    var log = App.Logger.log;

    var inits = {
        ApiList: {
            _$el: $("#data-container"),
            _read: '/oauth/console/apis/get',
            _update: '/oauth/console/apis/update',
            _destroy: '/oauth/console/apis/remove',
            _pageSize: 20,
            _client: 0,
            _breadcrumbs: [breadcrumbs.OAuth, "oauth_apis"],
            init: function () {
                var that = this;
                this._$el.find("#api-list").kendoGrid({
                    dataSource: that.dataSource(),
                    toolbar: ["create"],
                    pageable: true,
                    editable: 'popup',
                    columns: [
                        {field: "id", title: "id", width: 30, filterable: false},
                        {field: "name", title: "nazwa"},
                        {field: "domain", title: "domain"},
                        {field: "scopeRequestUrl", title: "scope url"},
                        {
                            field: "onGlobally",
                            title: " ",
                            width: 100,
                            template: '<span class=#= onGlobally ? "text-success" : "text-error"#>#=onGlobally ? true : false#</span>'
                        },
                        {
                            command: [{
                                text: "Scopes",
                                click: $.proxy(that.scopes, null, that._scopesWindow, that._downloadScopes, that._saveScopes)
                            }, "edit", "destroy"], title: "&nbsp;", width: 230
                        }
                    ]
                });
            },
            client: function (grid, client) {
                this._pageSize = 8;
                this._client = client;
                var that = this;
                grid.kendoGrid({
                    dataSource: that.dataSource(),
                    //toolbar: ["save", "cancel"],
                    pageable: true,
                    editable: 'popup',
                    columns: [
                        {field: "id", title: "id", width: 30, editable: false},
                        {field: "name", title: "nazwa", editable: false},
                        {field: "domain", title: "domain", editable: false},
                        {
                            field: "on",
                            width: 70,
                            template: '<span class=#= on ? "text-success" : "text-error"#>#=on ? true : false#</span>'
                        },
                        {
                            command: ["edit", {
                                text: "Edit scopes",
                                click: $.proxy(that.scopesEdit, null, that._scopesWindow, that._downloadScopes, that._saveScopes, client)
                            }], title: " ", width: "180px"
                        }
                    ]
                });
            },
            dataSource: function () {
                var that = this;
                var ds = new kendo.data.DataSource({
                    transport: {
                        read: that._read,
                        update: that._update,
                        create: that._update,
                        destroy: that._destroy,
                        dataType: "json",
                        parameterMap: function (options, operation) {
                            if (that._client !== undefined && that._client !== null && that._client > 0) {
                                options.clientId = that._client;
                            }
                            return {json: kendo.stringify(options)};
                        }
                    },
                    schema: {
                        model: {
                            id: "id",
                            fields: {
                                id: {editable: false, nullable: true},
                                name: {type: "string", editable: that._client === 0 ? true : false},
                                domain: {type: "string", editable: that._client === 0 ? true : false},
                                scopeRequestUrl: {type: "string", editable: that._client === 0 ? true : false},
                                on: {type: "boolean"},
                                onGlobally: {type: "boolean", editable: that._client === 0 ? true : false}
                            }
                        }
                    },
                    batch: true,
                    pageSize: that._pageSize,
                    serverPaging: true,
                    serverFiltering: true,
                    serverSorting: true
                });
                return ds;
            },
            _downloadScopes: function (e) {
                e.preventDefault();
                var target = $(e.currentTarget);
                var apiId = target.closest('div').find('input[name=api_id]').val();
                jsRoutes.oauth.COAuthConsole.downloadScopes(apiId).ajax({
                    success: function () {
                        $("#oauth-scopes-treeview").data("kendoTreeView").dataSource.read();
                    }
                });
            },
            _saveScopes: function (e) {
                e.preventDefault();
                var json = $("#oauth-scopes-form").serializeObject();
                jsRoutes.oauth.COAuthConsole.updateLevels(JSON.stringify(json)).ajax({
                    success: function () {
                        log("saved");
                    },
                    error: function () {
                        $("<div/>").kendoAlert({text: "error while saving"});
                    }
                });
            },
            _scopesWindow: function () {
                var kendoWindow;
                var wnd = $("#oauth-scopes-window");
                if (!wnd.data("kendoWindow")) {
                    wnd.kendoWindow({
                        width: 300,
                        animation: false,
                        appendTo: $("#data-container"), // inner container XXX FIXME
                        title: "Scopes",
                        actions: ["Refresh", "Close"],
                        open: function () {
                            this.element.find("#oauth-scopes-form").html('<div id="oauth-scopes-treeview"></div>');
                        },
                        refresh: function () {
                            $("#oauth-scopes-treeview").data("kendoTreeView").dataSource.read();
                        }
                    });
                    kendoWindow = wnd.data("kendoWindow");
                    kendoWindow.center().open();
                } else {
                    kendoWindow = wnd.data("kendoWindow");
                    kendoWindow.center().open();
                }
            },
            scopes: function (windowFunc, downloadFunc, saveFunc, e) {
                e.preventDefault();
                var apiId = this.dataItem($(e.currentTarget).closest('tr')).id;

                windowFunc();
                //this._scopesWindow();
                $("#oauth-scopes-form").append('<input type="hidden" name="api_id" value="' + apiId + '"/>');
                $("#oauth-scopes-treeview").kendoTreeView({
                    dataTextField: ["compositeId.level", "scopeUrl"],
                    dataSource: {
                        transport: {
                            read: {
                                url: jsRoutes.oauth.COAuthConsole.scopesForApi(apiId).url,
                                dataType: "json"
                            }
                        },
                        schema: {
                            model: {
                                id: "compositeId.level",
                                children: "scopes"
                            }
                        }
                    }
                });

                $("#oauth-scopes-window").find('.download-scopes').click(downloadFunc);
                $("#oauth-scopes-window").find('.save-scopes').click(saveFunc);

            },
            scopesEdit: function (windowFunc, downloadFunc, saveFunc, clientId, e) {

                e.preventDefault();
                var apiId = this.dataItem($(e.currentTarget).closest('tr')).id;
                windowFunc();

                $("#oauth-scopes-form").append('<input type="hidden" name="api_id" value="' + apiId + '"/><input type="hidden" name="client_id" value="' + clientId + '"/>');
                $("#oauth-scopes-treeview").kendoTreeView({
                    template: kendo.template($("#scopes-template").html()),
                    //dataTextField: ["compositeId.level", "scopeUrl"],
                    dataSource: {
                        transport: {
                            read: {
                                url: jsRoutes.oauth.COAuthConsole.scopesForApiAndClient(apiId, clientId).url,
                                dataType: "json"
                            }
                        },
                        schema: {
                            model: {
                                id: "compositeId.level",
                                children: "scopes"
                            }
                        }
                    }
                });
                $("#oauth-scopes-window").find('.download-scopes').click(downloadFunc);
                $("#oauth-scopes-window").find('.save-scopes').click(saveFunc);
            }
        },
        ClientList: { //FIXME jsRoute URLS
            _$el: $("#data-container"),
            _read: '/oauth/console/clients/get',
            _update: '/oauth/console/clients/update',
            _destroy: '/oauth/console/clients/remove',
            _pageSize: 20,
            _breadcrumbs: [breadcrumbs.OAuth, "oauth_clients"],
            init: function (e) {
                function detailInit(e) {
                    var detailRow = e.detailRow;
                    detailRow.find(".tabstrip").kendoTabStrip({
                        animation: false
                    });

                    var apis = detailRow.find(".apis");
                    var grid = $('<div class="api-list"></div>');
                    apis.html(grid);
                    var obs = kendo.observable(inits.ApiList);
                    kendo.bind(apis, obs);
                    obs.client(grid, e.data.id);
                }

                var that = this;
                this._$el.find("#client-list").kendoGrid({
                    dataSource: that.dataSource(),
                    detailTemplate: kendo.template($("#client-details-template").html()),
                    detailInit: detailInit,
                    filterable: GRID_FILTERABLE,
                    toolbar: ["create"],
                    pageable: true,
                    editable: 'popup',
                    columns: [
                        {field: "id", title: "id", width: 100},
                        {field: "name", title: "nazwa"},
                        {
                            field: "creationTime", title: "data utworzenia", template: function (dataItem) {
                            return moment(dataItem.creationTime * 1000).format('YYYY-MM-DD HH:mm');
                        }
                        },
                        {field: "apisSize", title: "ilosc aktywnych api", width: 150},
                        {command: ["edit", "destroy"], title: "&nbsp;", width: 160}
                    ],
                    edit: function (e) {
                        if (e.model.isNew()) {
                            $(".k-grid-edit-row").on("click", ".k-hierarchy-cell", function (e) {
                                e.preventDefault();
                                e.stopPropagation();
                            });
                        }
                    }
                });
            },
            dataSource: function () {
                var that = this;
                return new kendo.data.DataSource({
                    transport: {
                        read: that._read,
                        update: that._update,
                        create: that._update,
                        destroy: that._destroy,
                        dataType: "json",
                        parameterMap: function (options, operation) {
                            return {json: kendo.stringify(options)};
                        }
                    },
                    schema: {
                        model: {
                            id: "id",
                            fields: {
                                id: {editable: false, nullable: false, defaultValue: 0},
                                name: {type: "string"},
                                creationTime: {
                                    type: "number",
                                    editable: false,
                                    defaultValue: (new Date().getTime() / 1000) | 0
                                },
                                apisSize: {type: "number", editable: false}
                            }
                        }
                    },
                    batch: true,
                    pageSize: that._pageSize,
                    serverPaging: true,
                    serverFiltering: true,
                    serverSorting: true
                });
            }
        }
    };

    return inits;
});
